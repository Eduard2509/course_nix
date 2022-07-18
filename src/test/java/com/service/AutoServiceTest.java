package com.service;

import com.model.Auto;
import com.model.Manufacturer;
import com.repository.AutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

class AutoServiceTest {

    private AutoService target;
    private AutoRepository autoRepository;

    @BeforeEach
    void setUp() {
        autoRepository = Mockito.mock(AutoRepository.class);
        target = new AutoService(autoRepository);
    }

    @Test
    void createAutos_negativeCount() {
        final List<Auto> actual = target.createAndSaveAutos(-1);
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    void createAutos_zeroCount() {
        final List<Auto> actual = target.createAndSaveAutos(0);
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    void createAutos() {
        final List<Auto> actual = target.createAndSaveAutos(5);
        Assertions.assertEquals(5, actual.size());
        Mockito.verify(autoRepository, Mockito.times(5))
                .save(Mockito.any());
    }

    @Test
    void saveAutos() {
        List<Auto> autos = target.createAndSaveAutos(1);
        target.saveAutos(autos);
        Assertions.assertNotNull(autos);
        Mockito.verify(autoRepository).saveAll(autos);
    }

    @Test
    void printAll() {
        List<Auto> autos = List.of(createSimpleAuto(), createSimpleAuto());
        Mockito.when(autoRepository.getAll()).thenReturn(autos);
        target.printAll();
    }

    private Auto createSimpleAuto() {
        return new Auto("Model", Manufacturer.BMW, BigDecimal.ZERO, "Type");
    }

    @Test
    void findOneById_null1() {
        final Auto expected = createSimpleAuto();
        Mockito.when(autoRepository.getById("")).thenReturn(expected);
        final Auto actual = target.findOneById(null);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void findOneById_null2() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        target.findOneById(null);
        Mockito.verify(autoRepository).getById(captor.capture());
        Assertions.assertEquals("", captor.getValue());
    }

    @Test
    void deleteAuto_success() {
        target.delete("");
        Mockito.verify(autoRepository).delete("");
    }

    @Test
    void updateAutoByPrice_success() {
        final Auto auto = createSimpleAuto();
        auto.setPrice(BigDecimal.valueOf(280));
        Mockito.when(autoRepository.update(auto)).thenReturn(true);
        target.updateAuto(auto);
        Mockito.verify(autoRepository).update(auto);
        Assertions.assertEquals(BigDecimal.valueOf(280), auto.getPrice());
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

