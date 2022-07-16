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

    private final BusinessAutoRepository businessAutoRepository;
    private static final Random RANDOM = new Random();
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessAutoService.class);

    public BusinessAutoService(BusinessAutoRepository businessAutoRepository) {
        this.businessAutoRepository = businessAutoRepository;
    }

    public List<BusinessAuto> createAndSaveBusinessAutos (int count) {
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
            businessAutoRepository.save(businessAuto);
            LOGGER.debug("Created auto {}", businessAuto.getId());

        }
        return result;
    }

    public void updateBusinessAuto(BusinessAuto businessAuto) {
        businessAutoRepository.update(businessAuto);
    }

    public void updateBusinessAutoByPrice(String id, BigDecimal price) {
        BusinessAuto businessAuto = businessAutoRepository.getById(id);
        if(businessAuto == null) return;
        businessAuto.setPrice(price);
        businessAutoRepository.update(businessAuto);
    }


    public void deleteBusinessAuto(String id) {
        businessAutoRepository.delete(id);
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
        businessAutoRepository.saveAll(businessAutos);
    }

    public void printAll() {
        for (BusinessAuto businessAuto: businessAutoRepository.getAll()) {
            System.out.println(businessAuto);
        }
    }

    public BusinessAuto findOneById(String id) {
        if (id == null) {
            return businessAutoRepository.getById("");
        } else {
            return businessAutoRepository.getById(id);
        }
    }
}
