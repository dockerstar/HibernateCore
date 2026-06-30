package org.dmitri.HibernateCore;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("org.dmitri.HibernateCore");
        SessionFactory sessionFactory = context.getBean(SessionFactory.class);

        Session session = sessionFactory.openSession();

        Student student1 = new Student("Dima", 28);
        Student student2 = new Student("Vasya", 29);

        session.beginTransaction();
        session.persist(student1);
        session.persist(student2);
        session.getTransaction().commit();

        Student studentById1 = session.find(Student.class, 1L);
        System.out.println("Student1: " + studentById1.toString());

        Student studentById2 = session.createQuery(
                "select s from Student s where s.id=:id", Student.class)
                .setParameter("id", 2)
                .getSingleResult();

        System.out.println("Student2: " + studentById2.toString());

        session.beginTransaction();
        Student studentUpdateById1 = session.find(Student.class, 1L);
        studentUpdateById1.setAge(40);
        session.getTransaction().commit();

        Student findUpdateStudentById1 = session.createQuery(
                "select s from Student s where s.id=:id", Student.class
        )
                        .setParameter("id", 1)
                                .getSingleResult();
        System.out.println("Update Student 1: " + findUpdateStudentById1.toString());

        session.beginTransaction();
        Student studentForDelete = session.find(Student.class, 2L);
        session.remove(studentForDelete);
        session.getTransaction().commit();

        System.out.println("Table students: " + session.createQuery(
                "select s from Student s", Student.class
        )
                .getSingleResult());

        Student student3 = new Student("Alyona", 25);

        session.beginTransaction();
        session.persist(student3);
        session.getTransaction().commit();

        System.out.println("Table students: " + session.createQuery(
                        "select s from Student s", Student.class
                )
                .getResultList());

        session.beginTransaction();
        session.createNativeQuery(
                "delete from students s where s.id=1"
        ).executeUpdate();
        session.getTransaction().commit();

        System.out.println("Table students: " + session.createQuery(
                        "select s from Student s", Student.class
                )
                .getSingleResult());
        session.close();
    }
}
