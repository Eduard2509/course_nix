package com.repository;

import com.config.HibernateFactoryUtil;
import com.model.Auto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class HibernateAutoRepository implements CrudRepository<Auto> {
    private static HibernateAutoRepository instance;

    public static HibernateAutoRepository getInstance() {
        if (instance == null) {
            instance = new HibernateAutoRepository();
        }
        return instance;
    }

    @Override
    public Optional<Auto> findById(String id) {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Optional<Auto> businessAuto = session.createQuery(
                        "from Auto as p where p.id like :id", Auto.class)
                .setParameter("id", id)
                .uniqueResultOptional();
        session.close();
        return businessAuto;
    }

    @Override
    public List<Auto> getAll() {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Auto> autos = session.createQuery("from Auto", Auto.class)
                .list();
        session.close();
        return autos;
    }

    @Override
    public boolean save(Auto auto) {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(auto);
        session.flush();
        session.getTransaction().commit();
        return true;
    }

    @Override
    public boolean update(Auto auto) {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(auto);
        session.getTransaction().commit();
        session.close();
        return true;
    }


    @Override
    public boolean saveAll(List<Auto> autos) {
        if (autos == null) {
            return false;
        }
        autos.forEach(this::save);
        return true;
    }

    @Override
    public boolean delete(String id) {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Auto idAuto = session.createQuery("from Auto where id = :id", Auto.class)
                    .setParameter("id", id)
                    .getSingleResult();
            if (idAuto != null) {
                session.delete(idAuto);
                session.flush();
                session.getTransaction().commit();
            }
            return true;
        }
    }

    @Override
    public void clear() {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.createQuery("delete from Auto", Auto.class);
        session.close();
    }
}
