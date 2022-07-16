package com.service;


import com.model.Manufacturer;
import com.model.SportAuto;
import com.repository.SportAutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SportAutoServiceTest {

    private SportAutoService target;
    private SportAutoRepository sportAutoRepository;

    @BeforeEach
    void setUp() {
        sportAutoRepository = Mockito.mock(SportAutoRepository.class);
        target = new SportAutoService(sportAutoRepository);
    }

    @Test
    void createAutos_negativeCount() {
        final List<SportAuto> actual = target.createAndSaveSportAutos(-1);
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    void createAutos_zeroCount() {
        final List<SportAuto> actual = target.createAndSaveSportAutos(0);
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    void createAutos() {
        final List<SportAuto> actual = target.createAndSaveSportAutos(5);
        Assertions.assertEquals(5, actual.size());
        verify(sportAutoRepository, Mockito.times(5)).save(Mockito.any());
    }


    @Test
    void printAll() {
        List<SportAuto> autos = List.of(createSimpleAuto(), createSimpleAuto());
        Mockito.when(sportAutoRepository.getAll()).thenReturn(autos);
        target.printAll();
    }

    private SportAuto createSimpleAuto() {
        return new SportAuto("Model", Manufacturer.BMW, BigDecimal.ZERO, "Type", 180);
    }

    @Test
    void findOneById_null1() {
        final SportAuto expected = createSimpleAuto();
        Mockito.when(sportAutoRepository.getById("")).thenReturn(expected);
        final SportAuto actual = target.findOneById(null);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void findOneById_null2() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        target.findOneById(null);
        verify(sportAutoRepository).getById(captor.capture());
        Assertions.assertEquals("", captor.getValue());
    }

    @Test
    void findOneById_null3() {
        target.findOneById(null);
        verify(sportAutoRepository).getById(Mockito.argThat(new ArgumentMatcher<String>() {

            @Override
            public boolean matches(String s) {
                return s.isEmpty();
            }
        }));
    }


    @Test
    void updateSportAuto_fail() {
        SportAuto sportAutoCheck = createSimpleAuto();
        when(sportAutoRepository.update(sportAutoCheck)).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () -> target.updateSportAuto(sportAutoCheck));
    }


    @Test
    void updateSportAuto_price_null() {
        SportAuto sportAutoCheck = createSimpleAuto();
        sportAutoCheck.setPrice(null);
        when(sportAutoRepository.check(sportAutoCheck)).thenCallRealMethod();
        target.updateSportAuto(sportAutoCheck);
        verify(sportAutoRepository).update(sportAutoCheck);
        Assertions.assertEquals(BigDecimal.ZERO, sportAutoCheck.getPrice());
    }

    @Test
    void deleteSportAuto() {
        target.deleteSportAuto("");
        verify(sportAutoRepository).delete("");
    }


}