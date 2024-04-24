package com.emtech.ushurusmart.Etims.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    private String etimsCode;
    private String businessName;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    public Etims(String businessKRAPin, String businessOwnerKRAPin, String name) {
        this.businessKRAPin = businessKRAPin;
        this.businessOwnerKRAPin = businessOwnerKRAPin;
        this.name = name;
    }
}
