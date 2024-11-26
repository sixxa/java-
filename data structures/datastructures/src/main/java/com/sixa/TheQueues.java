package com.sixa;

import java.util.LinkedList;
import java.util.Queue;

public class TheQueues {
    public static void main(String[] args) {

        Queue<Person> queue = new LinkedList<>();
        queue.add(new Person("John", 18));
        queue.add(new Person("Jane", 17));
        queue.add(new Person("Jack", 18));
        queue.add(new Person("Jill", 18));

        System.out.println(queue.size());
        System.out.println(queue.peek());
        System.out.println(queue.poll());
        System.out.println(queue.size());
        System.out.println(queue.peek());
    }

    static record Person(String name, int age) {

    }
}
