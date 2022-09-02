package com.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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

    public static EntityManager getEntityManager() {
        if (entityManager == null) {
            EntityManagerFactory entityManagerFactory =
                    Persistence.createEntityManagerFactory("persistence");
            entityManager = entityManagerFactory.createEntityManager();
        }
        return entityManager;
    }

}
