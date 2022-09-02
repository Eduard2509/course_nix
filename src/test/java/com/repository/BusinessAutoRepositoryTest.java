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
import java.util.Optional;
import java.util.UUID;

class BusinessAutoRepositoryTest {

    private BusinessAutoRepository target;
    private BusinessAuto businessAuto;

    private BusinessAuto createSimpleBusinessAuto() {
        return new BusinessAuto(UUID.randomUUID().toString(), "Model", Manufacturer.BMW, BigDecimal.ZERO, BusinessClassAuto.A, 1);
    }

    @BeforeEach
    void setUp() {
        target = new BusinessAutoRepository();
        businessAuto = createSimpleBusinessAuto();
        target.save(businessAuto);
    }

    @Test
    void getById_findOne() {
        final Optional<BusinessAuto> actual = target.findById(businessAuto.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(businessAuto.getId(), actual.get().getId());
    }

    @Test
    void getById_notFind() {
        final Optional<BusinessAuto> actual = target.findById("333");
        Assertions.assertEquals(Optional.empty(), actual);
    }

    @Test
    void getById_findOne_manyAutos() {
        final BusinessAuto otherAuto = createSimpleBusinessAuto();
        target.save(otherAuto);
        final Optional<BusinessAuto> actual = target.findById(businessAuto.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(businessAuto.getId(), actual.get().getId());
        Assertions.assertNotEquals(otherAuto.getId(), actual.get().getId());
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
        final Optional<BusinessAuto> actualAuto = target.findById(businessAuto.getId());
        Assertions.assertEquals(BigDecimal.ONE, actualAuto.get().getPrice());
    }

    @Test
    void save_fail() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.save(null));
    }

    @Test
    void save_success_changePrice() {
        target.save(businessAuto);
        final Optional<BusinessAuto> actual = target.findById(businessAuto.getId());
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
        final Optional<BusinessAuto> actualAuto = target.findById(businessAuto.getId());
        Assertions.assertEquals(BigDecimal.TEN, actualAuto.get().getPrice());
    }


    @Test
    void delete_Success() {
        target.save(businessAuto);
        Assertions.assertTrue(target.delete(businessAuto.getId()));
    }
}