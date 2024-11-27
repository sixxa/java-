package com.sixa;

import java.util.LinkedList;
import java.util.ListIterator;

public class TheLinkedLists {
    public static void main(String[] args) {

        LinkedList<Person> list = new LinkedList();
        list.add(new Person("Alex", 21));
        list.add(new Person("Bob", 22));
        list.add(new Person("Carl", 23));
        list.add(new Person("Dan", 24));
        ListIterator<Person> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            System.out.println(listIterator.next());
        }
    }

    static record Person(String name, int age) {

    }
}
