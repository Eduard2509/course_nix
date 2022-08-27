package com.service;

import com.model.Auto;
import com.model.Engine;
import com.model.Manufacturer;
import com.repository.AutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class AutoServiceTest {

    private AutoService target;
    private AutoRepository autoRepository;

    @BeforeEach
    void setUp() {
        autoRepository = Mockito.mock(AutoRepository.class);
        target = new AutoService(autoRepository);
    }

    private Auto createSimpleAuto() {
        Engine engine = new Engine(3.3, "Sport");
        List<String> details = new ArrayList<>();
        details.add("door");
        details.add("Wildshield");
        details.add("Wheel");
        details.add("steering wheel");
        return new Auto("ID " + UUID.randomUUID().toString(),"Model", Manufacturer.BMW, BigDecimal.ZERO,
                "Type", 1, details, engine, "$", LocalDateTime.now());
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

