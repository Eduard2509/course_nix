package com.service;

import com.model.Manufacturer;
import com.model.SportAuto;
import com.repository.SportAutoRepository;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class SportAutoService extends VehicleService<SportAuto> {

    private static SportAutoService instance;
    private final SportAutoRepository sportAutoRepository;

    public SportAutoService(SportAutoRepository repository) {
        super(repository);
        this.sportAutoRepository = repository;
    }

    public static SportAutoService getInstance() {
        if (instance == null) {
            instance = new SportAutoService(SportAutoRepository.getInstance());
        }
        return instance;
    }

    @Override
    protected SportAuto creat() {
        return new SportAuto(
                "Model: " + RANDOM.nextInt(200),
                Manufacturer.BMW,
                BigDecimal.valueOf(RANDOM.nextDouble(200000.0)),
                "Sport",
                280, 1
        );
    }

    public void updateSportAuto(SportAuto sportAuto) {
        sportAutoRepository.check(sportAuto);
        repository.update(sportAuto);
    }


    public int findSportAuto(String id) {
        AtomicInteger atomicInteger = new AtomicInteger();
        repository.findById(id).ifPresentOrElse(
                sportAuto -> {
                    atomicInteger.set(sportAuto.getMaxSpeed());
                },
                () -> {
                    atomicInteger.set(0);
                }
        );
        return atomicInteger.get();
    }

    public void findSportAutosByBodyType(String bodyType) {
        sportAutoRepository.findByBodyType(bodyType)
                .filter(sportAuto -> sportAuto.getBodyType().equals(bodyType))
                .ifPresent(sportAuto -> repository.update(sportAuto));
    }

}
