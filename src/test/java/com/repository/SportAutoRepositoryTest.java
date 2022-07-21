package com.repository;

import com.model.Manufacturer;
import com.model.SportAuto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

class SportAutoRepositoryTest {

    private SportAutoRepository target;
    private SportAuto sportAuto;

    private SportAuto createSimpleSportAuto() {
        return new SportAuto("Model", Manufacturer.BMW, BigDecimal.ZERO, "Type", 180);
    }

    @BeforeEach
    void setUp() {
        target = new SportAutoRepository();
        sportAuto = createSimpleSportAuto();
        target.save(sportAuto);
    }

    @Test
    void getById_findOne() {
        final Optional<SportAuto> actual = target.findById(sportAuto.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(sportAuto.getId(), actual.get().getId());
    }

    @Test
    void getById_notFind() {
        final Optional<SportAuto> actual = target.findById("333");
        Assertions.assertEquals(Optional.empty(), actual);
    }

    @Test
    void getById_findOne_manyAutos() {
        final SportAuto otherAuto = createSimpleSportAuto();
        target.save(otherAuto);
        final Optional<SportAuto> actual = target.findById(sportAuto.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(sportAuto.getId(), actual.get().getId());
        Assertions.assertNotEquals(otherAuto.getId(), actual.get().getId());
    }

    @Test
    void getAll() {
        List<SportAuto> actual = target.getAll();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1, actual.size());
    }

    @Test
    void save_success_notChangePrice() {
        sportAuto.setPrice(BigDecimal.ONE);
        final boolean actual = target.save(sportAuto);
        Assertions.assertTrue(actual);
        final Optional<SportAuto> actualAuto = target.findById(sportAuto.getId());
        Assertions.assertEquals(BigDecimal.ONE, actualAuto.get().getPrice());
    }

    @Test
    void save_fail() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.save(null));
    }

    @Test
    void save_success_changePrice() {
        target.save(sportAuto);
        final Optional<SportAuto> actual = target.findById(sportAuto.getId());
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
        final boolean actual = target.saveAll(List.of(createSimpleSportAuto()));
        Assertions.assertTrue(actual);
    }

    @Test
    void update_notFound() {
        final SportAuto otherAuto = createSimpleSportAuto();
        final boolean actual = target.update(otherAuto);
        Assertions.assertFalse(actual);
    }

    @Test
    void update() {
        sportAuto.setPrice(BigDecimal.TEN);
        final boolean actual = target.update(sportAuto);
        Assertions.assertTrue(actual);
        final Optional<SportAuto> actualAuto = target.findById(sportAuto.getId());
        Assertions.assertEquals(BigDecimal.TEN, actualAuto.get().getPrice());
    }

    @Test
    void updateByBodyType() {
        final SportAuto otherAuto = createSimpleSportAuto();
        otherAuto.setManufacturer(Manufacturer.BMW);
        otherAuto.setPrice(BigDecimal.TEN);

        final boolean actual = target.updateByBodyType(sportAuto.getBodyType(), otherAuto);
        Assertions.assertTrue(actual);
        final Optional<SportAuto> actualAuto = target.findById(sportAuto.getId());
        Assertions.assertEquals(Manufacturer.BMW, actualAuto.get().getManufacturer());
        Assertions.assertEquals(BigDecimal.TEN, actualAuto.get().getPrice());
    }

    @Test
    void deleteSuccess() {
        final SportAuto sportAuto = createSimpleSportAuto();
        final List<SportAuto> autos = new LinkedList<>();
        autos.add(sportAuto);
        autos.remove(sportAuto);
        Assertions.assertEquals(Collections.emptyList(), autos);
    }


    @Test
    void getById() {
        Optional<SportAuto> auto = target.findById(sportAuto.getId());
        Assertions.assertNotNull(auto);
        Assertions.assertEquals(sportAuto.getMaxSpeed(), auto.get().getMaxSpeed());
        Assertions.assertEquals(sportAuto.getId(), auto.get().getId());
    }

    @Test
    void getById_null() {
        Optional<SportAuto> auto = target.findById(" ");
        Assertions.assertEquals(Optional.empty(), auto);
    }


    @Test
    void save() {
        target.save(sportAuto);
        Assertions.assertNotNull(sportAuto);
    }

    @Test
    void delete_Success() {
        target.save(sportAuto);
        Assertions.assertTrue(target.delete(sportAuto.getId()));

    }

}