package org.dmitri.EntityLC;

import org.dmitri.EntityLC.service.TicketService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("org.dmitri");

        TicketService ticketService = context.getBean(TicketService.class);
        SessionFactory sessionFactory = context.getBean(SessionFactory.class);

        //transient
        Ticket ticket1 = new Ticket("ticket1", LocalDate.now());
        Ticket ticket2 = new Ticket("ticket1", LocalDate.now());
        Ticket ticket3 = new Ticket("ticket1", LocalDate.now());
        ///


        //service
//        ticketService.save(ticket1);
//        ticketService.save(ticket2);
//        ticketService.save(ticket3);
//
//
//
//        ticketService.findAllTickets().forEach(System.out::println);
//
//
//        ticketService.deleteById(1L);
//        ticketService.findAllTickets().forEach(System.out::println);

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(ticket1);
            System.out.println("На данном этапе %s persistent".formatted(ticket1.getTitle()));
            session.getTransaction().commit();
        }

        ticket1.setTitle("detached"); // здесь hibernate уже не видет данный обьект, так как сессия закрылась

        try (Session session = sessionFactory.openSession()) {
            Ticket ticket = session.merge(ticket1); // hibernate переносит состояние detached обьекта в managed обьект!!
            System.out.println(ticket);
            session.beginTransaction();
            session.remove(ticket);
            session.getTransaction().commit();
        }


    }
}
