package org.dmitri.HibernateCore.Practic;

import org.dmitri.HibernateCore.HibernateConfiguration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class AppPractic {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("org.dmitri.HibernateCore");
        SessionFactory sessionFactory = context.getBean(SessionFactory.class);
        try (Session session = sessionFactory.openSession()) {
            Movie movie1 = new Movie("Movie1", "triller", 1900);
            Movie movie2 = new Movie("Movie2", "fantastic", 2000);
            Movie movie3 = new Movie("Movie3", "triller", 1950);

            session.beginTransaction();
            session.persist(movie1);
            session.persist(movie2);
            session.persist(movie3);
            session.getTransaction().commit();
        }

        try (Session session = sessionFactory.openSession()) {
            List<Movie> movieList = session.createQuery(
                    "select m from Movie m", Movie.class
            ).list();
            movieList.forEach(System.out::println);

            System.out.println(" ");

            List<Movie> moviesTriller = session.createQuery(
                            "select m FROM  Movie m where m.genre=:genre", Movie.class
                    )
                    .setParameter("genre", "Triller".toLowerCase())
                    .getResultList();
            moviesTriller.forEach(System.out::println);
        }

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Movie movieById1 = session.find(Movie.class, 1L);
            movieById1.setTitle("Movie1-update");
            session.getTransaction().commit();
        }

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Movie movieDeleteById = session.find(Movie.class, 3L);
            session.remove(movieDeleteById);
            session.getTransaction().commit();

            System.out.println("Оставшиеся фильмы: ");
            session.createQuery(
                            "select m from Movie m"
                    )
                    .list().forEach(System.out::println);
        }
    }
}
