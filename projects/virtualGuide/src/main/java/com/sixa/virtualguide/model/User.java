package com.sixa.virtualguide.model;

import com.sixa.virtualguide.model.converter.LanguagesConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING) // Map the enum to a string in the database
    private Role role;

    private String firstName;
    private String lastName;
    private String phone;
    private String bio;

    @Convert(converter = LanguagesConverter.class)
    private List<String> languages;
    @Embedded
    private Location location;
    //private xxx rating;

    // Add a reference to Account as the owning side
    @OneToOne
    @JoinColumn(name = "account_id")  // foreign key to Account
    private Account account;

    @ElementCollection
    @CollectionTable(name = "user_pictures", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "picture_path")
    private List<String> picturePaths; // Store file paths of uploaded pictures


}
