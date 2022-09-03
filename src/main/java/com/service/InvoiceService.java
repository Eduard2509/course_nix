package com.service;

import com.annotations.Autowired;
import com.model.*;
import com.repository.HibernateInvoiceRepository;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class InvoiceService {
    private static final AutoService AUTO_SERVICE = AutoService.getInstance();
    private static final BusinessAutoService BUSINESS_AUTO_SERVICE = BusinessAutoService.getInstance();
    private static final SportAutoService SPORT_AUTO_SERVICE = SportAutoService.getInstance();
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceService.class);


    private static InvoiceService instance;
    private static HibernateInvoiceRepository repository;

    @Autowired
    public InvoiceService(HibernateInvoiceRepository repository) {
        this.repository = repository;
    }

    public static InvoiceService getInstance() {
        if (instance == null) {
            instance = new InvoiceService(HibernateInvoiceRepository.getInstance());
        }
        return instance;
    }

    private Set<Vehicle> getRandomListVehicle(int count) {
        Set<Vehicle> listVehicle = new LinkedHashSet<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int randomVehicle = random.nextInt(0, 3);
            if (randomVehicle == 0) {
                Auto creat = AUTO_SERVICE.creat();
                AUTO_SERVICE.save(creat);
                listVehicle.add(creat);
            }
            if (randomVehicle == 1) {
                BusinessAuto creat = BUSINESS_AUTO_SERVICE.creat();
                BUSINESS_AUTO_SERVICE.save(creat);
                listVehicle.add(creat);
            }
            if (randomVehicle == 2) {
                SportAuto creat = SPORT_AUTO_SERVICE.creat();
                SPORT_AUTO_SERVICE.save(creat);
                listVehicle.add(creat);

            }
        }
        return listVehicle;
    }

    @SneakyThrows
    private BigDecimal getSumInvoice(Set<Vehicle> vehicles) {
        BigDecimal sum = BigDecimal.valueOf(0);
        for (Vehicle vehicle : vehicles) {
            BigDecimal price = vehicle.getPrice();
            sum = sum.add(price);
        }
        return sum;
    }

    public void createAndSaveRandomInvoice(int countVehicle) {
        Set<Vehicle> randomListVehicle = getRandomListVehicle(countVehicle);
        BigDecimal sumInvoice = getSumInvoice(randomListVehicle);
        Invoice invoice = new Invoice(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                randomListVehicle,
                sumInvoice);
        randomListVehicle.forEach(vehicle -> vehicle.setInvoice(invoice));
        repository.save(invoice);
        LOGGER.info("Invoice id created: {}", invoice.getId());
    }

    public void printAll() {
        Set<Invoice> all = repository.getAll();
        for (Invoice invoice : all) {
            System.out.println(invoice);
        }
    }

    public void save(Invoice invoice) {
        repository.save(invoice);
    }

    public void saveAll(List<Invoice> invoices) {
        repository.saveAll(invoices);
    }

    public void updateInvoice(Invoice invoice) {
        repository.update(invoice);
        LOGGER.info("Invoice update id: {}", invoice.getId());
    }

    public void deleteInvoiceById(String id) {
        repository.delete(id);
        LOGGER.info("Invoice deleted id: {}", id);
    }

    public void printInvoiceMorePrice(BigDecimal limit) {
        List<Invoice> invoiceMorePrice = repository.getInvoiceMorePrice(limit);
        for (Invoice invoice : invoiceMorePrice) {
            System.out.println(invoice);
        }
    }

    public void findInvoiceById(String id) {
        System.out.println(repository.findById(id).get());
    }

    public void printCountInvoiceInDB() {
        System.out.println("Count invoices: " + repository.getCountInvoiceInDB());
    }

    public void updateDateInvoice(String id) {
        repository.updateDateInvoice(id);
        LOGGER.info("Invoice date updated id: {}", id);
    }

    public Map groupInvoiceByPrice() {
        return repository.groupInvoiceByPrice();
    }

    public void clear() {
        repository.clear();
    }

    public void deleteAll() {
        AUTO_SERVICE.deleteAll();
        BUSINESS_AUTO_SERVICE.deleteAll();
        SPORT_AUTO_SERVICE.deleteAll();
        clear();
    }
}
