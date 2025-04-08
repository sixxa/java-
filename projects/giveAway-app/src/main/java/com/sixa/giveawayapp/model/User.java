package com.sixa.giveawayapp.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String email;
    private String password;
    private String number;

    @OneToMany(mappedBy = "user")
    private List<Item> items;

    @Enumerated(EnumType.STRING)
    private Role role;  // Role field to specify the user's role

}
