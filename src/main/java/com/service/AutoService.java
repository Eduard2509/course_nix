package com.service;

import com.model.Auto;
import com.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

public class AutoService extends VehicleService<Auto> {


    public AutoService(CrudRepository<Auto> repository) {
        super(repository);
    }

    @Override
    protected Auto creat() {
        return new Auto("Model-" + RANDOM.nextInt(1000),
                getRandomManufacturer(),
                BigDecimal.valueOf(RANDOM.nextDouble(1000.0)),
                "Model-" + RANDOM.nextInt(1000));
    }

    public void findAutoPrice(String id) {
        Auto aThrow = repository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                "Auto with id " + id + " is missing"));
        System.out.println(aThrow.getPrice());
    }

    public Auto findAutoBodyType(String id) {
        AtomicReference<Auto> atomicReference = new AtomicReference<>();
        repository.findById(id).ifPresent(auto -> {
            System.out.println(auto.getBodyType());
            atomicReference.set(auto);
        });
        return atomicReference.get();
    }

    public Auto createAutoWithoutId(String id) {
        Auto auto = repository.findById(id).orElseGet(() -> creat());
        System.out.println(auto.getId());
        return auto;
    }

}

