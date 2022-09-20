package com.concurrency;

import lombok.Getter;
import lombok.SneakyThrows;

import java.util.concurrent.Semaphore;

@Getter
public class Fabric {
    private final ContainerFuel containerFuel = new ContainerFuel();
    private final Semaphore semaphore = new Semaphore(1);
    FirstRobot firstRobot = new FirstRobot(containerFuel, semaphore);
    SecondAndThreeRobot secondAndThreeRobot = new SecondAndThreeRobot();
    FourthRobot fourthRobot = new FourthRobot();
    FifthRobot fifthRobot = new FifthRobot(containerFuel, semaphore);


    @SneakyThrows
    public void execute() {
        Thread firstRobotThread = new Thread(firstRobot);
        firstRobotThread.setDaemon(true);
        firstRobotThread.start();
        secondAndThreeRobot.start();
        secondAndThreeRobot.join();
        System.out.println("Second and three result: " + secondAndThreeRobot.getResult());
        fourthRobot.start();
        fourthRobot.join();
        System.out.println("Fourth result: " + fourthRobot.microschemas);
        System.out.println("fuel: " + containerFuel.capacity);
        Thread fifthRobotThread = new Thread(fifthRobot);
        fifthRobotThread.start();
        System.out.println("Fifths result: " + fifthRobot.point);
    }
}
