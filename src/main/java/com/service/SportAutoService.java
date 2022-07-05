package com.service;

import com.model.Manufacturer;
import com.model.SportAuto;
import com.repository.SportAutoRepository;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SportAutoService {

    private static final SportAutoRepository SPORT_AUTO_REPOSITORY = new SportAutoRepository();
    private static final Logger LOGGER = LoggerFactory.getLogger(SportAutoService.class);
    private static final Random RANDOM = new Random();

    public List<SportAuto> createSportAutos(int count) {
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
        //    LOGGER.debug("Created auto {}", sportAuto.getId());

        }
            return result;
    }

    public void saveSportAuto(List<SportAuto> sportAutos) {
        SPORT_AUTO_REPOSITORY.create(sportAutos);
    }

    public void printAll() {
        for (SportAuto sportAuto: SPORT_AUTO_REPOSITORY.getAll()) {
            System.out.println(sportAuto);
        }
    }
}
