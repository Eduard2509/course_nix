package com.service;

import com.annotations.Autowired;
import com.annotations.Singleton;
import com.model.BusinessAuto;
import com.model.BusinessClassAuto;
import com.model.Manufacturer;
import com.model.Vehicle;
import com.repository.BusinessAutoRepository;
import com.repository.DBBusinessAutoRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Singleton
public class BusinessAutoService extends VehicleService<BusinessAuto> {

    private static BusinessAutoService instance;

    private final DBBusinessAutoRepository businessAutoRepository;

    @Autowired
    public BusinessAutoService(DBBusinessAutoRepository repository) {
        super(repository);
        this.businessAutoRepository = repository;
    }

    public static BusinessAutoService getInstance() {
        if (instance == null) {
            instance = new BusinessAutoService(DBBusinessAutoRepository.getInstance());
        }
        return instance;
    }


    @Override
    protected BusinessAuto creat() {
        return new BusinessAuto(
                UUID.randomUUID().toString(),
                "Model: " + RANDOM.nextInt(100),
                getRandomManufacturer(),
                BigDecimal.valueOf(RANDOM.nextDouble(100.0)),
                getRandomBusinessClassAuto(),
                1
        );
    }

    private BusinessClassAuto getRandomBusinessClassAuto() {
        final BusinessClassAuto[] businessClass = BusinessClassAuto.values();
        final int num = RANDOM.nextInt(businessClass.length);
        return businessClass[num];
    }

    public Manufacturer findManufacturerBusinessAuto(String id) {
        StringBuilder sb = new StringBuilder();
        repository.findById(id)
                .map(Vehicle::getManufacturer)
                .ifPresent(sb::append);
        if (sb.isEmpty()) {
            return Manufacturer.NON;
        }
        return Manufacturer.valueOf(sb.toString());
    }


    public BusinessAuto findBusinessAuto(String id) {
        BusinessAuto businessAuto = businessAutoRepository.findById(id)
                .orElse(BusinessAutoUtil.SIMPLE_BUSINESS_AUTO);
        System.out.println(businessAuto);
        return businessAuto;
    }

}