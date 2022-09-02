package com.repository;

import com.config.HibernateFactoryUtil;
import com.model.BusinessAuto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class HibernateBusinessAutoRepository implements CrudRepository<BusinessAuto> {

    private static HibernateBusinessAutoRepository instance;

    public static HibernateBusinessAutoRepository getInstance() {
        if (instance == null) {
            instance = new HibernateBusinessAutoRepository();
        }
        return instance;
    }

    @Override
    public Optional<BusinessAuto> findById(String id) {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Optional<BusinessAuto> businessAuto = session.createQuery(
                        "from BusinessAuto as p where p.id like :id", BusinessAuto.class)
                .setParameter("id", id)
                .uniqueResultOptional();
        session.close();
        return businessAuto;
    }

    @Override
    public List<BusinessAuto> getAll() {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<BusinessAuto> businessAutos = session.createQuery(
                        "from BusinessAuto", BusinessAuto.class)
                .list();
        session.close();
        return businessAutos;
    }

    @Override
    public boolean save(BusinessAuto auto) {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(auto);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean saveAll(List<BusinessAuto> autos) {
        if (autos == null) {
            return false;
        }
        autos.forEach(this::save);
        return true;
    }

    @Override
    public boolean update(BusinessAuto auto) {
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
            BusinessAuto idAuto = session.createQuery("from BusinessAuto where id = :id", BusinessAuto.class)
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
        session.createQuery("delete from BusinessAuto ", BusinessAuto.class);
        session.close();
    }
}
