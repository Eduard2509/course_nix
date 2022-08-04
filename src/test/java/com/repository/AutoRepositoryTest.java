package com.repository;

import com.model.Auto;
import com.model.Engine;
import com.model.Manufacturer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class AutoRepositoryTest {

    private AutoRepository target;
    private Auto auto;


    private Auto createSimpleAuto() {
        Engine engine = new Engine(3.3, "Sport");

        List<String> details = new ArrayList<>();
        details.add("door");
        details.add("Wildshield");
        details.add("Wheel");
        details.add("steering wheel");
        return new Auto("Model", Manufacturer.BMW, BigDecimal.ZERO,
                "Type", 1, details, engine, "$", LocalDateTime.now());
    }

    @BeforeEach
    void setUp() {
        target = new AutoRepository();
        auto = createSimpleAuto();
        target.save(auto);
    }

    @Test
    void getById_findOne() {
        final Optional<Auto> actual = target.findById(auto.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(auto.getId(), actual.get().getId());
    }

    @Test
    void getById_notFind() {
        final Optional<Auto> actual = target.findById("1232");
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void getById_findOne_manyAutos() {
        final Auto otherAuto = createSimpleAuto();
        target.save(otherAuto);
        final Optional<Auto> actual = target.findById(auto.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(auto.getId(), actual.get().getId());
        Assertions.assertNotEquals(otherAuto.getId(), actual.get().getId());
    }

    @Test
    void getAll() {
        final List<Auto> actual = target.getAll();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1, actual.size());
    }

    @Test
    void save_success_notChangePrice() {
        auto.setPrice(BigDecimal.ONE);
        final boolean actual = target.save(auto);
        Assertions.assertTrue(actual);
        final Optional<Auto> actualAuto = target.findById(auto.getId());
        Assertions.assertEquals(BigDecimal.ONE, actualAuto.get().getPrice());
    }

    @Test
    void save_fail() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.save(null));
    }

    @Test
    void save_success_changePrice() {
        target.save(auto);
        final Optional<Auto> actual = target.findById(auto.getId());
        Assertions.assertEquals(BigDecimal.valueOf(-1), actual.get().getPrice());
    }

    @Test
    void saveAll_null() {
        final boolean actual = target.saveAll(null);
        Assertions.assertFalse(actual);
    }

    @Test
    void saveAll_emptyList() {
        final boolean actual = target.saveAll(Collections.emptyList());
        Assertions.assertFalse(actual);
    }

    @Test
    void saveAll() {
        final boolean actual = target.saveAll(List.of(createSimpleAuto()));
        Assertions.assertTrue(actual);
    }

    @Test
    void update_notFound() {
        final Auto otherAuto = createSimpleAuto();
        final boolean actual = target.update(otherAuto);
        Assertions.assertFalse(actual);
    }

    @Test
    void update() {
        auto.setPrice(BigDecimal.TEN);
        final boolean actual = target.update(auto);
        Assertions.assertTrue(actual);
        final Optional<Auto> actualAuto = target.findById(auto.getId());
        Assertions.assertTrue(actualAuto.isPresent());
        Assertions.assertEquals(BigDecimal.TEN, actualAuto.get().getPrice());
    }

    @Test
    void updateByBodyType() {
        final Auto otherAuto = createSimpleAuto();
        otherAuto.setManufacturer(Manufacturer.BMW);
        otherAuto.setPrice(BigDecimal.TEN);

        final boolean actual = target.updateByBodyType(auto.getBodyType(), otherAuto);
        Assertions.assertTrue(actual);
        final Optional<Auto> actualAuto = target.findById(auto.getId());
        Assertions.assertEquals(Manufacturer.BMW, actualAuto.get().getManufacturer());
        Assertions.assertEquals(BigDecimal.TEN, actualAuto.get().getPrice());
    }


    @Test
    void delete_Success1() {
        target.save(auto);
        Assertions.assertTrue(target.delete(auto.getId()));
    }
}
