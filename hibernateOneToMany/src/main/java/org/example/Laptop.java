package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Laptop {
@Id
    private int lid;
    private String lname;

    @ManyToOne
    private  Student student;

    public int getlid() {
        return lid;
    }

    public void setlid(int lid) {
        this.lid = lid;
    }

    public String getlname() {
        return lname;
    }

    public void setlname(String lname) {
        this.lname = lname;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
