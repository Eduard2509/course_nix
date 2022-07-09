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

public class AutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoService.class);
    private static final Random RANDOM = new Random();
    private static final AutoRepository AUTO_REPOSITORY = new AutoRepository();

    public List<Auto> createAutos(int count) {
        List<Auto> result = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            final Auto auto = new Auto(
                    "Model-" + RANDOM.nextInt(1000),
                    getRandomManufacturer(),
                    BigDecimal.valueOf(RANDOM.nextDouble(1000.0)),
                    "Model-" + RANDOM.nextInt(1000)
            );
            result.add(auto);
            LOGGER.debug("Created auto {}", auto.getId());
        }
        return result;
    }

    public void deleteAuto(String id) {
        AUTO_REPOSITORY.delete(id);
        LOGGER.debug("Deleted auto {}", id);
        }

    public void updateAuto(Auto auto) {
        AUTO_REPOSITORY.update(auto);
    }

    public void updateAuto(String id, BigDecimal price) {
       Auto auto = AUTO_REPOSITORY.getById(id);
       if (auto == null) return;
       auto.setPrice(price);
       AUTO_REPOSITORY.update(auto);
    }

    private Manufacturer getRandomManufacturer() {
        final Manufacturer[] values = Manufacturer.values();
        final int index = RANDOM.nextInt(values.length);
        return values[index];
    }

    public void saveAutos(List<Auto> autos) {
        AUTO_REPOSITORY.create(autos);
    }

    public void printAll() {
        for (Auto auto : AUTO_REPOSITORY.getAll()) {
            System.out.println(auto);
        }
    }
}
