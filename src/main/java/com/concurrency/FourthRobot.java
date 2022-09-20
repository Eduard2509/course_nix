package com.concurrency;

import lombok.SneakyThrows;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FourthRobot extends Thread {
    Random random = new Random();

    int microschemas = 0;

    @SneakyThrows
    @Override
    public void run() {
        System.out.println("FourthRobot started");
        while (microschemas <= 100) {
            System.out.println("Programmed microschemas: " + microschemas + "/100");
            int percent = random.nextInt(0, 10);
            if (percent > 3) {
                microschemas = microschemas + random.nextInt(25, 35);
                TimeUnit.SECONDS.sleep(1);
            } else {
                System.out.println("Microschemas crash, try again");
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }
}
