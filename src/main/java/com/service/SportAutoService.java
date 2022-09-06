package com.service;

import com.annotations.Autowired;
import com.annotations.Singleton;
import com.model.BusinessAuto;
import com.model.Manufacturer;
import com.model.SportAuto;
import com.repository.HibernateSportAutoRepository;
import com.repository.MongoSportAutoRepository;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class SportAutoService extends VehicleService<SportAuto> {

    private static SportAutoService instance;
    private final HibernateSportAutoRepository sportAutoRepository;

    @Autowired
    public SportAutoService(HibernateSportAutoRepository repository) {
        super(repository);
        this.sportAutoRepository = repository;
    }

    public static SportAutoService getInstance() {
        if (instance == null) {
            instance = new SportAutoService(HibernateSportAutoRepository.getInstance());
        }
        return instance;
    }

    @Override
    protected SportAuto creat() {
        return new SportAuto(
                UUID.randomUUID().toString(),
                "Model: " + RANDOM.nextInt(200),
                Manufacturer.BMW,
                BigDecimal.valueOf(RANDOM.nextDouble(200000.0)),
                "Sport",
                280, 1
        );
    }

    public void updateSportAuto(SportAuto sportAuto) {
        repository.update(sportAuto);
    }


    public int findSportAutoPrice(String id) {
        AtomicInteger atomicInteger = new AtomicInteger();
        sportAutoRepository.findById(id).ifPresentOrElse(
                sportAuto -> {
                    atomicInteger.set(sportAuto.getMaxSpeed());
                },
                () -> {
                    atomicInteger.set(0);
                }
        );
        return atomicInteger.get();
    }

    public SportAuto findSportAuto(String id) {
        return sportAutoRepository.findById(id).get();
    }
}
