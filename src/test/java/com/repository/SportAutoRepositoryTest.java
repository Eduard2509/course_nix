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
        final SportAuto actual = target.getById(sportAuto.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(sportAuto.getId(), actual.getId());
    }

    @Test
    void getById_notFind() {
        final SportAuto actual = target.getById("333");
        Assertions.assertNull(actual);
    }

    @Test
    void getById_findOne_manyAutos() {
        final SportAuto otherAuto = createSimpleSportAuto();
        target.save(otherAuto);
        final SportAuto actual = target.getById(sportAuto.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(sportAuto.getId(), actual.getId());
        Assertions.assertNotEquals(otherAuto.getId(), actual.getId());
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
        final SportAuto actualAuto = target.getById(sportAuto.getId());
        Assertions.assertEquals(BigDecimal.ONE, actualAuto.getPrice());
    }

    @Test
    void save_fail() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.save(null));
    }

    @Test
    void save_success_changePrice() {
        target.save(sportAuto);
        final SportAuto actual = target.getById(sportAuto.getId());
        Assertions.assertEquals(BigDecimal.valueOf(-1), actual.getPrice());
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
        final SportAuto actualAuto = target.getById(sportAuto.getId());
        Assertions.assertEquals(BigDecimal.TEN, actualAuto.getPrice());
    }

    @Test
    void updateByBodyType() {
        final SportAuto otherAuto = createSimpleSportAuto();
        otherAuto.setManufacturer(Manufacturer.BMW);
        otherAuto.setPrice(BigDecimal.TEN);

        final boolean actual = target.updateByBodyType(sportAuto.getBodyType(), otherAuto);
        Assertions.assertTrue(actual);
        final SportAuto actualAuto = target.getById(sportAuto.getId());
        Assertions.assertEquals(Manufacturer.BMW, actualAuto.getManufacturer());
        Assertions.assertEquals(BigDecimal.TEN, actualAuto.getPrice());
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
        SportAuto auto = target.getById(sportAuto.getId());
        Assertions.assertNotNull(auto);
        Assertions.assertEquals(sportAuto.getMaxSpeed(), auto.getMaxSpeed());
        Assertions.assertEquals(sportAuto.getId(), auto.getId());
    }

    @Test
    void getById_null() {
        SportAuto auto = target.getById(" ");
        Assertions.assertNull(auto);
    }

    @Test
    void getById_null2() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.getById(null));

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