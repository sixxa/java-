package com.sixa.virtualguide.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // Enum: USER, GUIDE, ADMIN (if applicable)

    // One-to-one relationship with User/Guide
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private User user;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private Guide guide;
}
