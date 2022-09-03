package com.repository;

import com.config.HibernateFactoryUtil;
import com.model.Auto;
import com.model.SportAuto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class HibernateSportAutoRepository implements CrudRepository<SportAuto> {

    private static HibernateSportAutoRepository instance;

    public static HibernateSportAutoRepository getInstance() {
        if (instance == null) {
            instance = new HibernateSportAutoRepository();
        }
        return instance;
    }

    @Override
    public Optional<SportAuto> findById(String id) {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Optional<SportAuto> auto = session.createQuery("from SportAuto where id = :id", SportAuto.class)
                .setParameter("id", id)
                .uniqueResultOptional();
        session.flush();
        session.getTransaction().commit();
        session.close();
        return auto;
    }

    @Override
    public List<SportAuto> getAll() {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<SportAuto> autos = session.createQuery("from SportAuto", SportAuto.class)
                .list();
        session.close();
        return autos;
    }

    @Override
    public boolean save(SportAuto auto) {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(auto);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean saveAll(List<SportAuto> autos) {
        if (autos == null) {
            return false;
        }
        autos.forEach(this::save);
        return true;
    }

    @Override
    public boolean update(SportAuto auto) {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(auto);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(String id) {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            SportAuto idAuto = session.createQuery("from SportAuto where id = :id", SportAuto.class)
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
