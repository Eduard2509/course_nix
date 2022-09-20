package com.concurrency;

import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Getter
public class SecondAndThreeRobot extends Thread {
    Random random = new Random();
    public int result = 0;

    @SneakyThrows
    @Override
    public void run() {
        System.out.println("Second and three robot started");
        while (result < 100) {
            System.out.println("Result work: " + result + "/100");
            result = result + random.nextInt(10, 20);
            TimeUnit.SECONDS.sleep(2);
        }
    }
}
