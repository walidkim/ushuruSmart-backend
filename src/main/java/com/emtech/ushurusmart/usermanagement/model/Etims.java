package com.emtech.ushurusmart.usermanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "etims")
public class Etims {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "business_kra_pin")
    private String businessKraPin;

    @Column(name = "business_owner_kra_pin")
    private String businessOwnerKraPin;


    @Column(name = "name")
    private String name;
}
