package com.sixa.giveawayapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}
