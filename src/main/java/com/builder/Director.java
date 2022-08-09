package com.builder;

import com.model.Auto;
import com.model.Engine;
import com.model.Manufacturer;
import com.model.VehicleType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Director {

    public Auto constructorAuto(Builder builder) {
        Engine engine = new Engine(3.3, "Sport");
        List<String> details = new ArrayList<>();
        details.add("door");
        details.add("Wildshield");
        details.add("Wheel");
        details.add("steering wheel");

        builder.setBodyType("BodyType");
        builder.setCount(1);
        builder.setCreated(LocalDateTime.now());
        builder.setPrice(BigDecimal.valueOf(850));
        builder.setCurrency("USD");
        builder.setDetails(details);
        builder.setEngine(engine);
        builder.setManufacturer(Manufacturer.BMW);
        builder.setModel("Model");
        builder.setId(UUID.randomUUID().toString());
        builder.setVehicleType(VehicleType.AUTO);
        return builder.getAuto();
    }
}
