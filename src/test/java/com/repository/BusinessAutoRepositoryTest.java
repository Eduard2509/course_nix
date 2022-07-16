package com.repository;

import com.model.BusinessAuto;
import com.model.BusinessClassAuto;
import com.model.Manufacturer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

class BusinessAutoRepositoryTest {

    private BusinessAutoRepository target;
    private BusinessAuto businessAuto;

    private BusinessAuto createSimpleBusinessAuto() {
        return new BusinessAuto("Model", Manufacturer.BMW, BigDecimal.ZERO, "Type", BusinessClassAuto.A);
    }

    @BeforeEach
    void setUp() {
        target = new BusinessAutoRepository();
        businessAuto = createSimpleBusinessAuto();
        target.save(businessAuto);
    }

    @Test
    void getById_findOne() {
        final BusinessAuto actual = target.getById(businessAuto.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(businessAuto.getId(), actual.getId());
    }

    @Test
    void getById_notFind() {
        final BusinessAuto actual = target.getById("333");
        Assertions.assertNull(actual);
    }

    @Test
    void getById_findOne_manyAutos() {
        final BusinessAuto otherAuto = createSimpleBusinessAuto();
        target.save(otherAuto);
        final BusinessAuto actual = target.getById(businessAuto.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(businessAuto.getId(), actual.getId());
        Assertions.assertNotEquals(otherAuto.getId(), actual.getId());
    }

    @Test
    void getAll() {
        List<BusinessAuto> actual = target.getAll();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1, actual.size());
    }

    @Test
    void save_success_notChangePrice() {
        businessAuto.setPrice(BigDecimal.ONE);
        final boolean actual = target.save(businessAuto);
        Assertions.assertTrue(actual);
        final BusinessAuto actualAuto = target.getById(businessAuto.getId());
        Assertions.assertEquals(BigDecimal.ONE, actualAuto.getPrice());
    }

    @Test
    void save_fail() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.save(null));
    }

    @Test
    void save_success_changePrice() {
        target.save(businessAuto);
        final BusinessAuto actual = target.getById(businessAuto.getId());
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
        final boolean actual = target.saveAll(List.of(createSimpleBusinessAuto()));
        Assertions.assertTrue(actual);
    }

    @Test
    void update_notFound() {
        final BusinessAuto otherAuto = createSimpleBusinessAuto();
        final boolean actual = target.update(otherAuto);
        Assertions.assertFalse(actual);
    }

    @Test
    void update() {
        businessAuto.setPrice(BigDecimal.TEN);
        final boolean actual = target.update(businessAuto);
        Assertions.assertTrue(actual);
        final BusinessAuto actualAuto = target.getById(businessAuto.getId());
        Assertions.assertEquals(BigDecimal.TEN, actualAuto.getPrice());
    }

    @Test
    void updateByBodyType() {
        final BusinessAuto otherAuto = createSimpleBusinessAuto();
        otherAuto.setManufacturer(Manufacturer.BMW);
        otherAuto.setPrice(BigDecimal.TEN);

        final boolean actual = target.updateByBodyType(businessAuto.getBodyType(), otherAuto);
        Assertions.assertTrue(actual);
        final BusinessAuto actualAuto = target.getById(businessAuto.getId());
        Assertions.assertEquals(Manufacturer.BMW, actualAuto.getManufacturer());
        Assertions.assertEquals(BigDecimal.TEN, actualAuto.getPrice());
    }

    @Test
    void delete_Success() {
        target.save(businessAuto);
        Assertions.assertTrue(target.delete(businessAuto.getId()));
    }
}