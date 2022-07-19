package com;

import com.discountUtil.DiscountUtil;
import com.model.Auto;
import com.model.Manufacturer;
import com.service.ProgramRun;

import java.math.BigDecimal;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        Random random = new Random();
        Auto auto = new Auto("Model-" + random.nextInt(1000),
                Manufacturer.BMW,
                BigDecimal.valueOf(random.nextDouble(1000.0)),
                "Model-" + random.nextInt(1000));

        DiscountUtil<Auto> autoDiscountUtil = new DiscountUtil<>(auto);
        autoDiscountUtil.printVehicle(auto);
        autoDiscountUtil.getDiscount(auto);
        autoDiscountUtil.upPrice(250);


        ProgramRun programRun = new ProgramRun();
        programRun.go();
    }
}