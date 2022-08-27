//package com.service;
//
//
//import com.model.BusinessAuto;
//import com.model.BusinessClassAuto;
//import com.model.Manufacturer;
//import com.repository.BusinessAutoRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//import java.util.UUID;
//
//class BusinessAutoServiceTest {
//
//    private BusinessAutoService target;
//    private BusinessAutoRepository businessAutoRepository;
//
//    @BeforeEach
//    void setUp() {
//        businessAutoRepository = Mockito.mock(BusinessAutoRepository.class);
//        target = new BusinessAutoService(businessAutoRepository);
//    }
//
//
//    private BusinessAuto createSimpleAuto() {
//        return new BusinessAuto(UUID.randomUUID().toString(), "Model", Manufacturer.BMW, BigDecimal.ZERO, BusinessClassAuto.A, 1);
//    }
//
//
//    @Test
//    void findManufacturerBusinessAuto_success() {
//        final BusinessAuto businessAuto = createSimpleAuto();
//        String id = businessAuto.getId();
//        Mockito.when(businessAutoRepository.findById(id)).thenReturn(Optional.of(businessAuto));
//        Manufacturer manufacturerBusinessAuto = target.findManufacturerBusinessAuto(id);
//        Mockito.verify(businessAutoRepository).findById(id);
//        Assertions.assertEquals(Manufacturer.BMW, manufacturerBusinessAuto);
//    }
//
//    @Test
//    void findManufacturerBusinessAuto_fail() {
//        String id = "";
//        Mockito.when(businessAutoRepository.findById(id)).thenReturn(Optional.empty());
//        Manufacturer manufacturer = target.findManufacturerBusinessAuto(id);
//        Assertions.assertEquals(Manufacturer.NON, manufacturer);
//    }
//
//    @Test
//    void findByBusinessClassAuto_success() {
//        final BusinessAuto businessAuto = createSimpleAuto();
//        BusinessClassAuto businessClass = businessAuto.getBusinessClassAuto();
//        Mockito.when(businessAutoRepository.findByBusinessClass(businessClass))
//                .thenReturn(Optional.of(businessAuto));
//        BusinessClassAuto businessClassAuto = target.findByBusinessClassAuto(businessClass);
//        Mockito.verify(businessAutoRepository).findByBusinessClass(businessClass);
//        Assertions.assertEquals(BusinessClassAuto.A, businessClassAuto);
//    }
//
//    @Test
//    void findByBusinessClassAuto_fail() {
//        BusinessClassAuto businessClassAuto = BusinessClassAuto.D;
//        Mockito.when(businessAutoRepository.findByBusinessClass(businessClassAuto))
//                .thenReturn(Optional.empty());
//        BusinessClassAuto businessClassAuto1 = target.findByBusinessClassAuto(businessClassAuto);
//        Assertions.assertEquals(BusinessClassAuto.A, businessClassAuto1);
//    }
//
//    @Test
//    void findBusinessAuto_success() {
//        final Optional<BusinessAuto> businessAuto = Optional.of(createSimpleAuto());
//        String id = businessAuto.get().getId();
//        Mockito.when(businessAutoRepository.findById(id)).thenReturn(businessAuto);
//        Optional<BusinessAuto> actual = Optional.of(target.findBusinessAuto(id));
//        Mockito.verify(businessAutoRepository).findById(id);
//        Assertions.assertEquals(Optional.of(actual.get().getId()), Optional.of(businessAuto.get().getId()));
//    }
//
//    @Test
//    void findBusinessAuto_fail() {
//        String id = "";
//        Mockito.when(businessAutoRepository.findById(id)).thenReturn(Optional.empty());
//        BusinessAuto actual = target.findBusinessAuto(id);
//        Mockito.verify(businessAutoRepository).findById(id);
//        Assertions.assertEquals(actual.getId(), BusinessAutoUtil.SIMPLE_BUSINESS_AUTO.getId());
//    }
//
//    @Test
//    void findBusinessAuto_fail_null() {
//        String id = null;
//        Mockito.when(businessAutoRepository.findById(id)).thenReturn(Optional.empty());
//        BusinessAuto actual = target.findBusinessAuto(id);
//        Mockito.verify(businessAutoRepository).findById(id);
//        Assertions.assertEquals(actual.getId(), BusinessAutoUtil.SIMPLE_BUSINESS_AUTO.getId());
//    }
//}
