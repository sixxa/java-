package com.sixa;

import java.util.HashSet;
import java.util.Set;

public class TheSets {
    public static void main(String[] args) {

        // no duplicates
        Set<BallS> balls = new HashSet<>();
        balls.add(new BallS("blue"));
        balls.add(new BallS("yellow"));
        balls.add(new BallS("red"));
        balls.forEach(ball -> System.out.println(ball));
    }

    record BallS(String color){ }
}
