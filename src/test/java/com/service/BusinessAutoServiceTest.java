package com.service;


import com.model.BusinessAuto;
import com.model.BusinessClassAuto;
import com.model.Manufacturer;
import com.repository.BusinessAutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

class BusinessAutoServiceTest {

    private BusinessAutoService target;
    private BusinessAutoRepository businessAutoRepository;

    @BeforeEach
    void setUp() {
        businessAutoRepository = Mockito.mock(BusinessAutoRepository.class);
        target = new BusinessAutoService(businessAutoRepository);
    }

    @Test
    void createAutos_negativeCount() {
        final List<BusinessAuto> actual = target.createAndSaveBusinessAutos(-1);
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    void createAutos_zeroCount() {
        final List<BusinessAuto> actual = target.createAndSaveBusinessAutos(0);
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    void createAutos() {
        final List<BusinessAuto> actual = target.createAndSaveBusinessAutos(5);
        Assertions.assertEquals(5, actual.size());
        Mockito.verify(businessAutoRepository, Mockito.times(5))
                .save(Mockito.any());
    }

    @Test
    void saveBusinessAutos() {
        List<BusinessAuto> businessAutos = target.createAndSaveBusinessAutos(1);
        target.saveBusinessAutos(businessAutos);
        Assertions.assertNotNull(businessAutos);
        Mockito.verify(businessAutoRepository).saveAll(businessAutos);
    }

    @Test
    void printAll() {
        List<BusinessAuto> autos = List.of(createSimpleAuto(), createSimpleAuto());
        Mockito.when(businessAutoRepository.getAll()).thenReturn(autos);
        target.printAll();
    }

    private BusinessAuto createSimpleAuto() {
        return new BusinessAuto("Model", Manufacturer.BMW, BigDecimal.ZERO, "Type", BusinessClassAuto.A);
    }

    @Test
    void findOneById_null1() {
        final BusinessAuto expected = createSimpleAuto();
        Mockito.when(businessAutoRepository.getById("")).thenReturn(expected);
        final BusinessAuto actual = target.findOneById(null);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void findOneById_null2() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        target.findOneById(null);
        Mockito.verify(businessAutoRepository).getById(captor.capture());
        Assertions.assertEquals("", captor.getValue());
    }


    @Test
    void updateBusinessAutoByPrice() {
        final BusinessAuto businessAuto = createSimpleAuto();
        businessAuto.setPrice(BigDecimal.valueOf(350));
        Mockito.when(businessAutoRepository.update(businessAuto)).thenReturn(true);
        target.updateBusinessAuto(businessAuto);
        Mockito.verify(businessAutoRepository).update(businessAuto);
        Assertions.assertEquals(BigDecimal.valueOf(350), businessAuto.getPrice());

    }

    @Test
    void deleteBusinessAuto_success() {
        target.deleteBusinessAuto("");
        Mockito.verify(businessAutoRepository).delete("");
    }

    @Test
    void findManufacturerBusinessAuto_success() {
        final BusinessAuto businessAuto = createSimpleAuto();
        String id = businessAuto.getId();
        Mockito.when(businessAutoRepository.findById(id)).thenReturn(Optional.of(businessAuto));
        Manufacturer manufacturerBusinessAuto = target.findManufacturerBusinessAuto(id);
        Mockito.verify(businessAutoRepository).findById(id);
        Assertions.assertEquals(Manufacturer.BMW, manufacturerBusinessAuto);
    }

    @Test
    void findManufacturerBusinessAuto_fail() {
        String id = "";
        Mockito.when(businessAutoRepository.findById(id)).thenReturn(Optional.empty());
        Manufacturer manufacturer = target.findManufacturerBusinessAuto(id);
        Assertions.assertEquals(Manufacturer.NON, manufacturer);
    }

    @Test
    void findByBusinessClassAuto_success() {
        final BusinessAuto businessAuto = createSimpleAuto();
        BusinessClassAuto businessClass = businessAuto.getBusinessClassAuto();
        Mockito.when(businessAutoRepository.findByBusinessClass(businessClass))
                .thenReturn(Optional.of(businessAuto));
        BusinessClassAuto businessClassAuto = target.findByBusinessClassAuto(businessClass);
        Mockito.verify(businessAutoRepository).findByBusinessClass(businessClass);
        Assertions.assertEquals(BusinessClassAuto.A, businessClassAuto);
    }

    @Test
    void findByBusinessClassAuto_fail() {
        BusinessClassAuto businessClassAuto = BusinessClassAuto.D;
        Mockito.when(businessAutoRepository.findByBusinessClass(businessClassAuto))
                .thenReturn(Optional.empty());
        BusinessClassAuto businessClassAuto1 = target.findByBusinessClassAuto(businessClassAuto);
        Assertions.assertEquals(BusinessClassAuto.A, businessClassAuto1);
    }

    @Test
    void findBusinessAuto_success() {
        final BusinessAuto businessAuto = createSimpleAuto();
        String id = businessAuto.getId();
        Mockito.when(businessAutoRepository.findById(id)).thenReturn(Optional.of(businessAuto));
        BusinessAuto actual = target.findBusinessAuto(id);
        Mockito.verify(businessAutoRepository).findById(id);
        Assertions.assertEquals(actual.getId(), businessAuto.getId());
    }

    @Test
    void findBusinessAuto_fail() {
        String id = "";
        Mockito.when(businessAutoRepository.findById(id)).thenReturn(Optional.empty());
        BusinessAuto actual = target.findBusinessAuto(id);
        Mockito.verify(businessAutoRepository).findById(id);
        Assertions.assertEquals(actual.getId(), BusinessAutoUtil.SIMPLE_BUSINESS_AUTO.getId());
    }

    @Test
    void findBusinessAuto_fail_null() {
        String id = null;
        Mockito.when(businessAutoRepository.findById(id)).thenReturn(Optional.empty());
        BusinessAuto actual = target.findBusinessAuto(id);
        Mockito.verify(businessAutoRepository).findById(id);
        Assertions.assertEquals(actual.getId(), BusinessAutoUtil.SIMPLE_BUSINESS_AUTO.getId());
    }
}
