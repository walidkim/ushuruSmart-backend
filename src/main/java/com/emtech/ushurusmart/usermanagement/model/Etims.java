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
    private String businessKRAPin;


    private String businessOwnerKRAPin;
    private String name;


    public Etims(String businessKRAPin, String businessOwnerKRAPin, String name) {
        this.businessKRAPin = businessKRAPin;
        this.businessOwnerKRAPin = businessOwnerKRAPin;
        this.name = name;
    }
}



