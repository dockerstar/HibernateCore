package org.dmitri.EntityLC.service;

import org.dmitri.EntityLC.Ticket;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    private final SessionFactory sessionFactory;

    public TicketService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Ticket ticket) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(ticket);
            session.getTransaction().commit();
            System.out.println("Ticket saved:\n%s".formatted(ticket));
        }
    }

    public Ticket getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Ticket ticket = session.find(Ticket.class, id);
            return ticket;
        }
    }

    public List<Ticket> findAllTickets() {
        try (Session session = sessionFactory.openSession()) {
            List<Ticket> ticketList = session.createQuery(
                    "select t from Ticket t", Ticket.class
            ).list();
            return ticketList;
        }
    }

    public void deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Ticket ticket = getById(id);
            session.remove(ticket);
            session.getTransaction().commit();
        }
    }
}
