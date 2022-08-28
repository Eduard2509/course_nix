package com.service;

import com.annotations.Autowired;
import com.annotations.Singleton;
import com.model.Manufacturer;
import com.model.SportAuto;
import com.repository.DBSportAutoRepository;
import com.repository.SportAutoRepository;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class SportAutoService extends VehicleService<SportAuto> {

    private static SportAutoService instance;
    private final DBSportAutoRepository sportAutoRepository;

    @Autowired
    public SportAutoService(DBSportAutoRepository repository) {
        super(repository);
        this.sportAutoRepository = repository;
    }

    public static SportAutoService getInstance() {
        if (instance == null) {
            instance = new SportAutoService(DBSportAutoRepository.getInstance());
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


}
