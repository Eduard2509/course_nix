package com.service;

import com.model.BusinessAuto;
import com.model.BusinessClassAuto;
import com.model.Manufacturer;
import com.repository.BusinessAutoRepository;

import java.math.BigDecimal;
import java.util.Optional;

public class BusinessAutoService extends VehicleService<BusinessAuto> {


    private final BusinessAutoRepository businessAutoRepository;

    public BusinessAutoService(BusinessAutoRepository repository) {
        super(repository);
        this.businessAutoRepository = repository;
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
                "Mercedes",
                getRandomBusinessClassAuto()
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
                .map(businessAuto -> businessAuto.getManufacturer())
                .ifPresent(manufacturer -> sb.append(manufacturer));
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