package com.sixa;

import java.util.Arrays;

public class TheArrays {
        public static void main(String[] args) {
            String[] colors = new String[2];
            int[] num = {100,200};
            colors[0] = "red";
            colors[1] = "green";

            System.out.println(Arrays.toString(colors));

            for (int num1 : num) {
                System.out.println(num1);
            }

            Arrays.stream(colors).forEach(System.out::println);
        }
}

