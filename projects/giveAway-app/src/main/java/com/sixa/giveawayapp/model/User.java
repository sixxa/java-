package com.sixa.giveawayapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Data;
import jakarta.persistence.GenerationType;
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
    @JsonManagedReference
    private List<Item> items;

    @Enumerated(EnumType.STRING)
    private Role role;  // Role field to specify the user's role

}
