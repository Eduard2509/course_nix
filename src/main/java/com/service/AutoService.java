package com.service;

import com.model.Auto;
import com.model.Manufacturer;
import com.repository.AutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class AutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoService.class);
    private static final Random RANDOM = new Random();
    private final AutoRepository autoRepository;

    public AutoService(AutoRepository autoRepository) {
        this.autoRepository = autoRepository;
    }

    public List<Auto> createAndSaveAutos(int count) {
        List<Auto> result = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            final Auto auto = new Auto(
                    "Model-" + RANDOM.nextInt(1000),
                    getRandomManufacturer(),
                    BigDecimal.valueOf(RANDOM.nextDouble(1000.0)),
                    "Model-" + RANDOM.nextInt(1000)
            );
            result.add(auto);
            autoRepository.save(auto);
            LOGGER.debug("Created auto {}", auto.getId());
        }
        return result;
    }

    private Manufacturer getRandomManufacturer() {
        final Manufacturer[] values = Manufacturer.values();
        final int index = RANDOM.nextInt(values.length);
        return values[index];
    }

    public void saveAutos(List<Auto> autos) {
        autoRepository.saveAll(autos);
    }

    public void printAll() {
        for (Auto auto : autoRepository.getAll()) {
            System.out.println(auto);
        }
    }

    public void findAutoPrice(String id) {
        Auto aThrow = autoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                "Auto with id " + id + " is missing"));
        System.out.println(aThrow.getPrice());
    }

    public Auto findAutoBodyType(String id) {
        AtomicReference<Auto> atomicReference = new AtomicReference<>();
        autoRepository.findById(id).ifPresent(auto -> {
            System.out.println(auto.getBodyType());
            atomicReference.set(auto);
        });
        return atomicReference.get();
    }

    public Auto createAutoWithoutId(String id) {
        Auto auto = autoRepository.findById(id).orElseGet(() -> createOne());
        System.out.println(auto.getId());
        return auto;
    }


    public Auto findOneById(String id) {
        if (id == null) {
            return autoRepository.getById("");
        } else {
            return autoRepository.getById(id);
        }
    }

    public void delete(String id) {
        autoRepository.delete(id);
        LOGGER.debug("Deleted auto {}", id);
    }

    public void updateAuto(Auto auto) {
        autoRepository.update(auto);
    }

    public void updateAutoByPrice(String id, BigDecimal price) {
        Auto auto = autoRepository.getById(id);
        if (auto == null) return;
        auto.setPrice(price);
        autoRepository.update(auto);
    }

    private Auto createOne() {
        return new Auto(
                "Model new",
                getRandomManufacturer(),
                BigDecimal.valueOf(RANDOM.nextDouble(1000.0)),
                "Model-" + RANDOM.nextInt(1000)
        );
    }

}
