package com.repository;

import com.config.HibernateFactoryUtil;
import com.model.Invoice;
import com.model.Vehicle;
import com.model.VehicleType;
import com.service.InvoiceService;
import lombok.SneakyThrows;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class HibernateInvoiceRepository {
    private static final HibernateAutoRepository AUTO_REPOSITORY = HibernateAutoRepository.getInstance();
    private static final HibernateBusinessAutoRepository BUSINESS_AUTO_REPOSITORY =
            HibernateBusinessAutoRepository.getInstance();
    private static final HibernateSportAutoRepository SPORT_AUTO_REPOSITORY = HibernateSportAutoRepository.getInstance();
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceService.class);
    private static HibernateInvoiceRepository instance;

    public static HibernateInvoiceRepository getInstance() {
        if (instance == null) {
            instance = new HibernateInvoiceRepository();
        }
        return instance;
    }

    @SneakyThrows
    public Set<Invoice> getAll() {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Invoice> invoices = session.createQuery("from Invoice", Invoice.class)
                .list();
        session.close();
        return new HashSet<>(invoices);
    }


    private BigDecimal getSumInvoice(Invoice invoice) {
        BigDecimal sum = BigDecimal.valueOf(0);
        Set<Vehicle> vehicles = invoice.getVehicles();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleType().equals(VehicleType.AUTO)) {
                sum = sum.add(vehicle.getPrice());
            }
            if (vehicle.getVehicleType().equals(VehicleType.BUSINESS_AUTO)) {
                sum = sum.add(vehicle.getPrice());
            }
            if (vehicle.getVehicleType().equals(VehicleType.SPORT_AUTO)) {
                sum = sum.add(vehicle.getPrice());
            }
        }
        return sum;
    }

    @SneakyThrows
    public boolean save(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("BusinessAuto must not be null");
        }
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(invoice);
        session.flush();
        session.getTransaction().commit();
        session.close();
        return true;
    }

    public boolean saveAll(List<Invoice> invoices) {
        if (invoices == null) {
            return false;
        }
        invoices.forEach(this::save);
        return true;
    }

    @SneakyThrows
    public boolean update(Invoice invoice) {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(invoice);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @SneakyThrows
    public boolean delete(String id) {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Invoice idInvoice = session.createQuery("from Invoice where id = :id", Invoice.class)
                    .setParameter("id", id)
                    .getSingleResult();
            if (idInvoice != null) {
                session.delete(idInvoice);
                session.flush();
                session.getTransaction().commit();
            }
            return true;
        }
    }

    @SneakyThrows
    public Optional<Invoice> findById(String id) {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Optional<Invoice> invoice = session.createQuery(
                        "from Invoice as p where p.id like :id", Invoice.class)
                .setParameter("id", id)
                .uniqueResultOptional();
        session.close();
        return invoice;
    }

    @SneakyThrows
    public int getCountInvoiceInDB() {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        long count = session.createQuery(
                        "select count(distinct p.id) from Invoice p", Long.class)
                .getSingleResult();
        session.close();
        return Math.toIntExact(count);
    }

    @SneakyThrows
    public void updateDateInvoice(String id) {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Optional<Invoice> optionalInvoice = findById(id);
        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            invoice.setCreated(LocalDateTime.now());
            session.update(invoice);
            session.flush();
            session.close();
        }
    }

    @SneakyThrows
    public Map groupInvoiceByPrice() { //https://www.onlinetutorialspoint.com/hibernate/hibernate-groupby-criteria-and-hql-query-example.html
        SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("select i.price, count (i.price) " +
                "from Invoice i group by i.price");
        List<Object[]> list = query.getResultList();
        Map<Object, Integer> groupedPrices = new HashMap<>();
        list.forEach(item -> groupedPrices.put(item[0], Integer.valueOf(item[1].toString())));
        transaction.commit();
        session.close();
        return groupedPrices;
    }

    @SneakyThrows
    public void clear() {
        AUTO_REPOSITORY.clear();
        BUSINESS_AUTO_REPOSITORY.clear();
        SPORT_AUTO_REPOSITORY.clear();
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.createQuery("delete from Invoice ", Invoice.class);
        session.close();
    }


    @SneakyThrows
    public List<Invoice> getInvoiceMorePrice(BigDecimal priceLimit) {
        final SessionFactory sessionFactory = HibernateFactoryUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Invoice> invoices = session.createQuery(
                        "from Invoice as p where p.price > :limit", Invoice.class)
                .setParameter("limit", priceLimit)
                .stream()
                .toList();
        invoices.forEach(invoice -> Hibernate.initialize(invoice.getVehicles()));
        session.close();
        return invoices;
    }
}

