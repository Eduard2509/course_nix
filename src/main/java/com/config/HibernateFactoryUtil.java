package com.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.EntityManager;

public class HibernateFactoryUtil {

    private static SessionFactory sessionFactory;
    private static EntityManager entityManager;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
           final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                   .configure()
                   .build();
           try {
               sessionFactory = new MetadataSources(registry)
                       .buildMetadata()
                       .buildSessionFactory();
           } catch (Exception e) {
               StandardServiceRegistryBuilder.destroy(registry);
               throw e;
           }
        }
        return sessionFactory;
    }
}
