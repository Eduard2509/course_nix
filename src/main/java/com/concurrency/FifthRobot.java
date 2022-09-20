package com.concurrency;

import lombok.SneakyThrows;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class FifthRobot implements Runnable {

    Random random = new Random();
    int point = 0;
    final ContainerFuel containerFuel;
    final Semaphore semaphore;

    public FifthRobot(ContainerFuel containerFuel, Semaphore semaphore) {
        this.containerFuel = containerFuel;
        this.semaphore = semaphore;
    }

    @SneakyThrows
    @Override
    public void run() {
        System.out.println("Start work fifth robot: ");
        while (point < 100) {
            int usedFuel = random.nextInt(350, 700);
            if (containerFuel.capacity - usedFuel > 0) {
                containerFuel.capacity = containerFuel.capacity - usedFuel;
                point = point + 10;
                System.out.println("point: " + point + "/100");
                System.out.println("remaining fuel: " + containerFuel.capacity);
                TimeUnit.SECONDS.sleep(1);
            } else {
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }
}
