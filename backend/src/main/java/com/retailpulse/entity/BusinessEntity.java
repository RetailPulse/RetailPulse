package com.retailpulse.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class BusinessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private boolean external = false;

    @Column(nullable = false)
    private boolean active = true;
}
