package org.example;

public class Alien {
    private int age;
    private Laptop laptop;
    private Computer com;  // Ensure this matches the type of your bean

    public Alien() {
        System.out.println("alien object created");
    }

    public void code() {
        System.out.println("coding..");
        if (com != null) {
            com.compile();  // Ensure 'com' is initialized before this line
        } else {
            System.out.println("Computer bean is not injected!");
        }
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Computer getCom() {
        return com;
    }

    public void setCom(Computer com) {
        this.com = com;
    }

    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }

    public Laptop getLaptop() {
        return laptop;
    }
}
