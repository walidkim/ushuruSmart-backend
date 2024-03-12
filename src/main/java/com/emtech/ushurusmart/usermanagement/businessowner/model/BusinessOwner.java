package com.emtech.ushurusmart.usermanagement.businessowner.model;

import com.emtech.ushurusmart.usermanagement.businessowner.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="business_owner")
@Data
public class BusinessOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "BusinessKRAPin")
    private String BusinessKRAPin;

    @Column(name = "BusinessOwnerKRAPin")
    private String BusinessOwnerKRAPin;

    @Column(name = "BusinessOwnerUsername")
    private String BusinessOwnerUsername;

    @Column(name = "BusinessOwnerBusinessName")
    private String BusinessOwnerBusinessName;

    @Column(name = "email")
    private String email;

    @Column(name = "PhoneNumber")
    private Integer PhoneNumber;

    @Column(name = "Password")
    private String Password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

}
