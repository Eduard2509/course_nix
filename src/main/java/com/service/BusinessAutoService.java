package com.service;

import com.model.BusinessAuto;
import com.model.BusinessClassAuto;
import com.model.Manufacturer;
import com.repository.BusinessAutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BusinessAutoService {

    private static final BusinessAutoRepository BUSINESS_AUTO_REPOSITORY = new BusinessAutoRepository();
    private static final Random RANDOM = new Random();
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessAutoService.class);

    public List<BusinessAuto> createBusinessAutos (int count) {
        List<BusinessAuto> result = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            final BusinessAuto businessAuto = new BusinessAuto(
                    "Model: " + RANDOM.nextInt(100),
                    getRandomManufacturer(),
                    BigDecimal.valueOf(RANDOM.nextDouble(100.0)),
                    "Mercedes",
                    getRandomBusinessClassAuto()
            );
            result.add(businessAuto);
            LOGGER.debug("Created auto {}", businessAuto.getId());

        }
        return result;
    }

    public void updateBusinessAuto(BusinessAuto businessAuto) {
        BUSINESS_AUTO_REPOSITORY.update(businessAuto);
    }

    public void updateBusinessAuto(String id, BigDecimal price) {
        BusinessAuto businessAuto = BUSINESS_AUTO_REPOSITORY.getById(id);
        if(businessAuto == null) return;
        businessAuto.setPrice(price);
        BUSINESS_AUTO_REPOSITORY.update(businessAuto);
    }

    public void deleteBusinessAuto(String id) {
        BUSINESS_AUTO_REPOSITORY.delete(id);
        LOGGER.debug("Deleted auto {}", id);
    }

    private Manufacturer getRandomManufacturer() {
        final Manufacturer[] values = Manufacturer.values();
        final int index = RANDOM.nextInt(values.length);
        return values[index];
    }

    private BusinessClassAuto getRandomBusinessClassAuto(){
        final BusinessClassAuto[] businessClass = BusinessClassAuto.values();
        final int num = RANDOM.nextInt(businessClass.length);
        return businessClass[num];
    }

    public void saveBusinessAutos(List<BusinessAuto> businessAutos) {
        BUSINESS_AUTO_REPOSITORY.create(businessAutos);
    }

    public void printAll() {
        for (BusinessAuto businessAuto: BUSINESS_AUTO_REPOSITORY.getAll()) {
            System.out.println(businessAuto);
        }
    }
}
