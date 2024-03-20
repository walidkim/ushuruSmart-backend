package com.emtech.ushurusmart.usermanagement.model;

import com.emtech.ushurusmart.usermanagement.repository.EtimsRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.lang.module.Configuration;
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


    public Etims(String businessKRAPin, String businessOwnerKRAPin, String name) {
        this.businessKRAPin = businessKRAPin;
        this.businessOwnerKRAPin = businessOwnerKRAPin;
        this.name = name;
    }
}



