package com.murphy.jdbc.demo;

import com.murphy.jdbc.entity.Course;
import com.murphy.jdbc.entity.Instructor;
import com.murphy.jdbc.entity.InstructorDetail;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class FetchJoinDemo {

    public static void main(String[] args) {

        // create session factory
        SessionFactory factory = new Configuration()
                                .configure("hibernate.cfg.xml")
                                .addAnnotatedClass(Instructor.class)
                                .addAnnotatedClass(InstructorDetail.class)
                                .addAnnotatedClass(Course.class)
                                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // start a transaction
            session.beginTransaction();

            // option 2: Hibernate query with HQL

            // get courses from db
            int theId = 1;

            // when executed, will load instructor and courses all at once
            Query<Instructor> query =
                    session.createQuery("SELECT i FROM Instructor i " +
                                        "JOIN FETCH i.courses " +
                                        "WHERE i.id=:theInstructorId",
                                        Instructor.class
                    );

            // set parameter on query
            query.setParameter("theInstructorId", theId);

            // execute query and get instructor
            Instructor tempInstructor = query.getSingleResult();

            System.out.println("luv2code: Instructor: " + tempInstructor);

            // commit transaction
            session.getTransaction().commit();

            // close the session
            session.close();

            System.out.println("\nluv2code: The session is now closed!\n");

            // get courses for instructor
            System.out.println("luv2code: Courses: " + tempInstructor.getCourses());

            System.out.println("luv2code: Done!");

        } finally {

            // add clean up code
            session.close();

            factory.close();
        }
    }
}
