package com.service;

import com.model.*;

import java.math.BigDecimal;
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
        return switch (type) {
            case AUTO -> new Auto(
                    "Model-" + RANDOM.nextInt(1000),
                    getRandomManufacturer(),
                    BigDecimal.valueOf(RANDOM.nextDouble(1000.0)),
                    "Model-" + RANDOM.nextInt(1000), 1
            );

            case BUSINESSAUTO -> new BusinessAuto(
                    "Model: " + RANDOM.nextInt(100),
                    getRandomManufacturer(),
                    BigDecimal.valueOf(RANDOM.nextDouble(100.0)),
                    getRandomBusinessClassAuto(),
                    1
            );

            case SPORTAUTO -> new SportAuto("Model: " + RANDOM.nextInt(200),
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