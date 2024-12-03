package com.sixa.virtualguide.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING) // Map the enum to a string in the database
    private Role role;

    private String firstName;
    private String lastName;
    private String phone;
    private String bio;
    @Embedded
    private Location location;
    //private xxx rating;


}
