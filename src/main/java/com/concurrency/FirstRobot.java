package com.concurrency;

import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Getter
public class FirstRobot implements Runnable {
    final ContainerFuel containerFuel;
    final Semaphore semaphore;

    public FirstRobot(ContainerFuel containerFuel, Semaphore semaphore) {
        this.containerFuel = containerFuel;
        this.semaphore = semaphore;
    }

    Random random = new Random();

    public void createFuel() {
        int i = random.nextInt(500, 1000);
        containerFuel.capacity = containerFuel.capacity + i;
    }

    @SneakyThrows
    @Override
    public void run() {
        System.out.println("First robot started");
        while (true) {
            createFuel();
            TimeUnit.SECONDS.sleep(3);
        }
    }
}
