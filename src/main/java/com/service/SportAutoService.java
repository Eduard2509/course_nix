package com.service;

import com.model.BusinessAuto;
import com.model.Manufacturer;
import com.model.SportAuto;
import com.repository.SportAutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SportAutoService {

    private final SportAutoRepository sportAutoRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(SportAutoService.class);
    private static final Random RANDOM = new Random();

    public SportAutoService(SportAutoRepository sportAutoRepository) {
        this.sportAutoRepository = sportAutoRepository;
    }

    public List<SportAuto> createAndSaveSportAutos(int count) {
        List<SportAuto> result = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            final SportAuto sportAuto = new SportAuto(
                    "Model: " + RANDOM.nextInt(200),
                    Manufacturer.BMW,
                    BigDecimal.valueOf(RANDOM.nextDouble(200000.0)),
                    "Sport",
                    280
            );
            result.add(sportAuto);
            sportAutoRepository.save(sportAuto);
            LOGGER.debug("Created auto {}", sportAuto.getId());

        }
            return result;
    }

    public void updateSportAuto(SportAuto sportAuto) {
        sportAutoRepository.check(sportAuto);
        sportAutoRepository.update(sportAuto);
    }

    public void updateSportAutoByPrice(String id, BigDecimal price) {
        SportAuto sportAuto = sportAutoRepository.getById(id);
        if (sportAuto == null) return;
        sportAuto.setPrice(price);
        sportAutoRepository.update(sportAuto);
    }

    public void deleteSportAuto(String id) {
        sportAutoRepository.delete(id);
        LOGGER.debug("Deleted auto {}", id);
    }

    public void saveSportAuto(List<SportAuto> sportAutos) {
        sportAutoRepository.saveAll(sportAutos);
    }

    public void printAll() {
        for (SportAuto sportAuto: sportAutoRepository.getAll()) {
            System.out.println(sportAuto);
        }
    }

    public SportAuto findOneById(String id) {
        if (id == null) {
            return sportAutoRepository.getById("");
        } else {
            return sportAutoRepository.getById(id);
        }
    }
}
