package com.service;

import com.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VehicleFactory {
    private static VehicleFactory instance;

    private static final Random RANDOM = new Random();


    private VehicleFactory() {
    }

    public static VehicleFactory getInstance() {
        if (instance == null) {
            instance = new VehicleFactory();
        }
        return instance;
    }

    public Vehicle build(VehicleType type) {
        Engine engine = new Engine(3.3, "Sport");
        List<String> details = new ArrayList<>();
        details.add("door");
        details.add("Windshield");
        details.add("Wheel");
        details.add("steering wheel");
        return switch (type) {
            case AUTO -> new Auto(
                    "Model-" + RANDOM.nextInt(1000),
                    getRandomManufacturer(),
                    BigDecimal.valueOf(RANDOM.nextDouble(1000.0)),
                    "Model-" + RANDOM.nextInt(1000),
                    1, details, engine, "$", LocalDateTime.now()
            );

            case BUSINESS_AUTO -> new BusinessAuto(
                    "Model: " + RANDOM.nextInt(100),
                    getRandomManufacturer(),
                    BigDecimal.valueOf(RANDOM.nextDouble(100.0)),
                    getRandomBusinessClassAuto(),
                    1
            );

            case SPORT_AUTO -> new SportAuto("Model: " + RANDOM.nextInt(200),
                    Manufacturer.BMW,
                    BigDecimal.valueOf(RANDOM.nextDouble(200000.0)),
                    "Sport",
                    280, 1);

            default -> throw new IllegalArgumentException("Cannot build " + type);
        };
    }

    private Manufacturer getRandomManufacturer() {
        final Manufacturer[] values = Manufacturer.values();
        final int index = RANDOM.nextInt(values.length);
        return values[index];
    }

    private BusinessClassAuto getRandomBusinessClassAuto() {
        final BusinessClassAuto[] businessClass = BusinessClassAuto.values();
        final int num = RANDOM.nextInt(businessClass.length);
        return businessClass[num];
    }
}
