package org.example;

public class Alien {
    private int age;
    private Laptop laptop;

    public Alien(){
        System.out.println("alien object created");
    }

    public Alien(int age) {
        this.age = age;
    }

    public void code(){
        System.out.println("coding..");
        laptop.compile();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Laptop getLaptop() {
        return laptop;
    }

    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }
}
