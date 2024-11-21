package com.sixa;

import java.util.ArrayList;
import java.util.List;

public class TheLists {
    public static void main(String[] args) {

        List<String> colors = new ArrayList<>();
        colors.add("red");
        colors.add("blue");
        colors.add("green");

        System.out.println(colors);

        for (String color : colors) {
            System.out.println(color);
        }

        colors.forEach(color -> System.out.println(color));

    }
}
