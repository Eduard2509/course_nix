package com.service;

import com.model.Auto;
import com.model.Engine;
import com.model.Manufacturer;
import com.repository.AutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;

class VehicleServiceTest {

    private VehicleService<Auto> target;

    private AutoRepository repository;

    private Auto auto;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(AutoRepository.class);
        target = new VehicleService<>(repository) {
            @Override
            protected Auto creat() {
                return createSimpleAuto();
            }
        };
        auto = createSimpleAuto();
    }

    private Auto createSimpleAuto() {
        Engine engine = new Engine(3.3, "Sport");
        List<String> details = new ArrayList<>();
        details.add("door");
        details.add("Wildshield");
        details.add("Wheel");
        details.add("steering wheel");
        return new Auto("Model", Manufacturer.BMW,
                BigDecimal.valueOf(1000.0), "Type", 1, details, engine, "$", LocalDateTime.now());
    }

    @Test
    void createAutos_negativeCount() {
        final List<Auto> actual = target.createAndSave(-1);
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    void createAutos_zeroCount() {
        final List<Auto> actual = target.createAndSave(0);
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    void createAutos() {
        final List<Auto> actual = target.createAndSave(5);
        Assertions.assertEquals(5, actual.size());
        Mockito.verify(repository, Mockito.times(5))
                .save(Mockito.any());
    }

    @Test
    void saveAutos() {
        List<Auto> autos = target.createAndSave(1);
        target.saveAllVehicles(autos);
        Assertions.assertNotNull(autos);
        Mockito.verify(repository).saveAll(autos);
    }


    @Test
    void findOneById_null1() {
        final Auto expected = createSimpleAuto();
        Mockito.when(repository.findById("")).thenReturn(Optional.of(expected));
        final Optional<Auto> actual = target.findOneById(null);
        Assertions.assertEquals(Optional.of(expected.getId()), Optional.of(actual.get().getId()));
    }

    @Test
    void findOneById_null2() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        target.findOneById(null);
        Mockito.verify(repository).findById(captor.capture());
        Assertions.assertEquals("", captor.getValue());
    }

    @Test
    void findOneById_null3() {
        target.findOneById(null);
        verify(repository).findById(Mockito.argThat(new ArgumentMatcher<String>() {

            @Override
            public boolean matches(String s) {
                return s.isEmpty();
            }
        }));
    }

    @Test
    void updateAutoByPrice_success() {
        final Auto auto = createSimpleAuto();
        auto.setPrice(BigDecimal.valueOf(280));
        Mockito.when(repository.update(auto)).thenReturn(true);
        target.update(auto);
        Mockito.verify(repository).update(auto);
        Assertions.assertEquals(BigDecimal.valueOf(280), auto.getPrice());
    }

    @Test
    void deleteAuto_success() {
        target.delete("");
        Mockito.verify(repository).delete("");
    }

    @Test
    void deleteAuto_fail() {
        target.delete("");
        Mockito.verify(repository, Mockito.never()).delete("333");
    }

}