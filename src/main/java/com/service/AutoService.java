package com.service;

import com.model.Auto;
import com.model.Manufacturer;
import com.repository.AutoRepository;
import com.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class AutoService extends VehicleService<Auto> {

    private static AutoService instance;

    public AutoService(CrudRepository<Auto> repository) {
        super(repository);
    }

    public static AutoService getInstance() {
        if (instance == null) {
            instance = new AutoService(AutoRepository.getInstance());
        }
        return instance;
    }

    @Override
    protected Auto creat() {
        List<String> details = new ArrayList<>();
        details.add("door");
        details.add("Wildshield");
        details.add("Wheel");
        details.add("steering wheel");
        return new Auto("Model-" + RANDOM.nextInt(1000),
                getRandomManufacturer(),
                BigDecimal.valueOf(RANDOM.nextDouble(1000.0)),
                "Model-" + RANDOM.nextInt(1000), 1, details);
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


    public boolean checkDetailAuto(List<Auto> autos, String detail) {
        return autos.stream()
                .flatMap(auto -> auto.getDetails().stream())
                .anyMatch(auto -> auto.equals(detail));
    }

    Function<Map<String, Object>, Auto> createVehicleByMap = stringObjectMap -> {
        List<String> details = new ArrayList<>();
        details.add("Door");
        details.add("Wildshield");
        details.add("Wheel");
        details.add("Steering wheel");

        Object model = stringObjectMap.getOrDefault("Model", "Model0");
        Object type = stringObjectMap.getOrDefault("Type", "Type0");
        Object manufacturer = stringObjectMap.getOrDefault("Manufacturer", Manufacturer.NON);
        Object price = stringObjectMap.getOrDefault("Price", BigDecimal.ZERO);
        Object count = stringObjectMap.getOrDefault("Count", 0);
        Object details1 = stringObjectMap.getOrDefault("details", details);

        return new Auto((String) model,
                (Manufacturer) manufacturer,
                (BigDecimal) price,
                (String) type,
                (int) count,
                (List<String>) details1);
    };

}

