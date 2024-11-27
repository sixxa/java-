package com.sixa;

import java.util.HashMap;
import java.util.Map;

public class TheMaps {
    public static void main(String[] args) {

        //no duplicates
        Map<Integer, Person> map = new HashMap<>();
        map.put(1, new Person("Alex"));
        map.put(2, new Person("Bob"));
        map.put(3, new Person("Carl"));

        System.out.println(map);
        System.out.println(map.get(1));
        System.out.println(map.size());
        System.out.println(map.keySet());
        System.out.println(map.entrySet());

        map.entrySet().forEach(System.out::println);
    }

    record Person(String name){}
}
