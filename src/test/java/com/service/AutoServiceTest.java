package com.service;

import com.model.Auto;
import com.model.Manufacturer;
import com.repository.AutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

class AutoServiceTest {

    private AutoService target;
    private AutoRepository autoRepository;

    @BeforeEach
    void setUp() {
        autoRepository = Mockito.mock(AutoRepository.class);
        target = new AutoService(autoRepository);
    }

    private Auto createSimpleAuto() {
        return new Auto("Model", Manufacturer.BMW, BigDecimal.ZERO, "Type");
    }

    @Test
    void findAutoPrice_success() {
        Auto auto = createSimpleAuto();
        String id = auto.getId();
        Mockito.when(autoRepository.findById(id)).thenReturn(Optional.of(auto));
        target.findAutoPrice(id);
        Mockito.verify(autoRepository).findById(id);
    }

    @Test
    void findAutoPrice_fail() {
        String id = "";
        Mockito.when(autoRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.findAutoPrice(id));
    }

    @Test
    void findAutoBodyType_success() {
        Auto auto = createSimpleAuto();
        String id = auto.getId();
        Mockito.when(autoRepository.findById(id)).thenReturn(Optional.of(auto));
        Auto type = target.findAutoBodyType(id);
        Mockito.verify(autoRepository).findById(id);
        Assertions.assertEquals(auto.getBodyType(), type.getBodyType());
    }

    @Test
    void findAutoBodyType_fail() {
        String id = "";
        Mockito.when(autoRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertNull(target.findAutoBodyType(id));
    }

    @Test
    void createAutoWithoutId_success() {
        final Auto actualAuto = createSimpleAuto();
        String id = actualAuto.getId();
        Mockito.when(autoRepository.findById(id)).thenReturn(Optional.of(actualAuto));
        Auto testAuto = target.createAutoWithoutId(id);
        Mockito.verify(autoRepository).findById(id);
        Assertions.assertEquals(actualAuto, testAuto);
    }

    @Test
    void createAutoWithoutId_fail() {
        String id = "";
        Mockito.when(autoRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertNotNull(target.createAutoWithoutId(id));
    }

}

