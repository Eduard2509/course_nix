//package com.service;
//
//
//import com.model.Manufacturer;
//import com.model.SportAuto;
//import com.repository.SportAutoRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//class SportAutoServiceTest {
//
//    private SportAutoService target;
//    private SportAutoRepository sportAutoRepository;
//
//    @BeforeEach
//    void setUp() {
//        sportAutoRepository = Mockito.mock(SportAutoRepository.class);
//        target = new SportAutoService(sportAutoRepository);
//    }
//
//    private SportAuto createSimpleAuto() {
//        return new SportAuto(UUID.randomUUID().toString(),"Model", Manufacturer.BMW, BigDecimal.ZERO, "Type", 180, 1);
//    }
//
//
//    @Test
//    void updateSportAuto_fail() {
//        SportAuto sportAutoCheck = createSimpleAuto();
//        when(sportAutoRepository.update(sportAutoCheck)).thenThrow(RuntimeException.class);
//        Assertions.assertThrows(RuntimeException.class, () -> target.updateSportAuto(sportAutoCheck));
//    }
//
//
//    @Test
//    void updateSportAuto_price_null() {
//        SportAuto sportAutoCheck = createSimpleAuto();
//        sportAutoCheck.setPrice(null);
//        when(sportAutoRepository.check(sportAutoCheck)).thenCallRealMethod();
//        target.updateSportAuto(sportAutoCheck);
//        verify(sportAutoRepository).update(sportAutoCheck);
//        Assertions.assertEquals(BigDecimal.ZERO, sportAutoCheck.getPrice());
//    }
//
//
//    @Test
//    void findSportAuto_success() {
//        final SportAuto sportAuto = createSimpleAuto();
//        String id = sportAuto.getId();
//        Mockito.when(sportAutoRepository.findById(id)).thenReturn(Optional.of(sportAuto));
//        int maxSpeed = target.findSportAuto(id);
//        verify(sportAutoRepository).findById(id);
//        Assertions.assertEquals(sportAuto.getMaxSpeed(), maxSpeed);
//    }
//
//    @Test
//    void findSportAuto_fail() {
//        String id = "";
//        Mockito.when(sportAutoRepository.findById(id)).thenReturn(Optional.empty());
//        int maxSpeed = target.findSportAuto(id);
//        Assertions.assertEquals(0, maxSpeed);
//    }
//
////    @Test
////    void findSportAutosByBodyType_success() {
////        final Optional<SportAuto> sportAuto = Optional.of(createSimpleAuto());
////        String type = sportAuto.get().getBodyType();
////        Mockito.when(sportAutoRepository.findByBodyType(type)).thenReturn(sportAuto);
////        target.findSportAutosByBodyType(type);
////        Mockito.verify(sportAutoRepository).update(Mockito.any());
////        Mockito.verify(sportAutoRepository).findByBodyType(type);
////    }
////
////    @Test
////    void findSportAutosByBodyType_fail() {
////        String bodyType = "Type";
////        Mockito.when(sportAutoRepository.findByBodyType(bodyType)).thenReturn(Optional.empty());
////        Mockito.verify(sportAutoRepository, Mockito.never()).update(Mockito.any());
////    }
//
//}