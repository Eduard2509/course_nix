package com.service;

import com.annotations.Autowired;
import com.config.JDBCConfig;
import com.model.*;
import com.repository.DBAutoRepository;
import com.repository.DBBusinessAutoRepository;
import com.repository.DBInvoiceRepository;
import com.repository.DBSportAutoRepository;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class InvoiceService {
    private static final DBAutoRepository DB_AUTO_REPOSITORY = DBAutoRepository.getInstance();
    private static final DBBusinessAutoRepository DB_BUSINESS_AUTO_REPOSITORY = DBBusinessAutoRepository.getInstance();
    private static final DBSportAutoRepository DB_SPORT_AUTO_REPOSITORY = DBSportAutoRepository.getInstance();
    private static final AutoService AUTO_SERVICE = AutoService.getInstance();
    private static final BusinessAutoService BUSINESS_AUTO_SERVICE = BusinessAutoService.getInstance();
    private static final SportAutoService SPORT_AUTO_SERVICE = SportAutoService.getInstance();
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceService.class);

    private static InvoiceService instance;
    private static DBInvoiceRepository repository;

    @Autowired
    public InvoiceService(DBInvoiceRepository repository) {
        this.repository = repository;
    }

    public static InvoiceService getInstance() {
        if (instance == null) {
            instance = new InvoiceService(DBInvoiceRepository.getInstance());
        }
        return instance;
    }

    private List<Vehicle> getRandomListVehicle(int count) {
        List<Vehicle> listVehicle = new ArrayList<Vehicle>();
        Random random = new Random();
        int randomVehicle = random.nextInt(0, 3);
        for (int i = 0; i < count; i++) {
            if (randomVehicle == 0) {
                listVehicle.add(AUTO_SERVICE.creat());
            }
            if (randomVehicle == 1) {
                listVehicle.add(BUSINESS_AUTO_SERVICE.creat());
            }
            if (randomVehicle == 2) {
                listVehicle.add(SPORT_AUTO_SERVICE.creat());
            }
        }
        return listVehicle;
    }


    public void creatAndSaveRandomInvoice(int countVehicle){
        Invoice invoice = new Invoice(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                getRandomListVehicle(countVehicle));
        repository.save(invoice);
        LOGGER.info("Invoice id created: {}", invoice.getId());
    }

    public void printAll(){
        List<Invoice> all = repository.getAll();
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

    public void findInvoiceById(String id) {
        System.out.println(repository.findById(id));
    }

    private BigDecimal sumInvoice(Invoice invoice) {
        BigDecimal sum = BigDecimal.valueOf(0);
        invoice.getVehicles().stream()
                .map(Vehicle::getPrice)
                .forEach(sum::add);
        return sum;
    }

    @SneakyThrows
    public List<Invoice> getInvoiceMorePrice(BigDecimal priceLimit) {
        List<Invoice> invoices = repository.getAll();
        List<Invoice> result = new ArrayList<>();
        for (Invoice invoice : invoices) {
            BigDecimal sumInvoice = sumInvoice(invoice);
            if (sumInvoice.compareTo(priceLimit) > 0) {
                result.add(invoice);
            }
        }
        return result;
    }

}
