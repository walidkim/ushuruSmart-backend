package com.emtech.ushurusmart.usermanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "etims")
public class Etims {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "BusinessKRAPin")
    private String businessKRAPin;

    @Column(name = "BusinessOwnerKRAPin")
    private String businessOwnerKRAPin;

    @Column(name = "name")
    private String name;
}
