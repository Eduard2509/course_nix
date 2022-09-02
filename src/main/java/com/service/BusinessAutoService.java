package com.service;

import com.annotations.Autowired;
import com.annotations.Singleton;
import com.model.BusinessAuto;
import com.model.BusinessClassAuto;
import com.model.Manufacturer;
import com.model.Vehicle;
import com.repository.BusinessAutoRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Singleton
public class BusinessAutoService extends VehicleService<BusinessAuto> {

    private static BusinessAutoService instance;

    private final BusinessAutoRepository businessAutoRepository;

    @Autowired
    public BusinessAutoService(BusinessAutoRepository repository) {
        super(repository);
        this.businessAutoRepository = repository;
    }

    public static BusinessAutoService getInstance() {
        if (instance == null) {
            instance = new BusinessAutoService(BusinessAutoRepository.getInstance());
        }
        return instance;
    }

    public void updateBusinessAuto(BusinessAuto businessAuto) {
        repository.update(businessAuto);
    }

    @Override
    protected BusinessAuto creat() {
        return new BusinessAuto(
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

    public BusinessClassAuto findByBusinessClassAuto(BusinessClassAuto businessClassAuto) {
        Optional<BusinessAuto> businessAuto = businessAutoRepository
                .findByBusinessClass(businessClassAuto)
                .or(() -> Optional.of(BusinessAutoUtil.SIMPLE_BUSINESS_AUTO));
        return businessAuto.get().getBusinessClassAuto();
    }

    public BusinessAuto findBusinessAuto(String id) {
        BusinessAuto businessAuto = businessAutoRepository.findById(id)
                .orElse(BusinessAutoUtil.SIMPLE_BUSINESS_AUTO);
        System.out.println(businessAuto);
        return businessAuto;
    }

}