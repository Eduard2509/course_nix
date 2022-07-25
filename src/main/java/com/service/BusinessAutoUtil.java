package com.service;

import com.model.BusinessAuto;
import com.model.BusinessClassAuto;
import com.model.Manufacturer;

import java.math.BigDecimal;

public class BusinessAutoUtil {

    public static final BusinessAuto SIMPLE_BUSINESS_AUTO =
            new BusinessAuto("Model",
                    Manufacturer.BMW,
                    BigDecimal.TEN, BusinessClassAuto.A, 1);

}
