package com.emtech.ushurusmart.usermanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name="kra_unique",columnNames = "KRAPin")
        }
)
public class Admin {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "admin_sequence"
    )
    @Column(
            nullable = false
    )
    private Long id;
    @Column(
            nullable = false
    )
    private String KRAPin;
    @Column(
            nullable = false
    )
    private String businessName;
    @Column(
            columnDefinition = "TEXT",
            nullable = false
    )
    private String fullName;
    @Column(
            nullable = false
    )
    private String password;
    @Column(
            nullable = false
    )
    private Integer phoneNumber;
    @OneToMany(mappedBy = "admin",cascade = CascadeType.ALL)
    private List<Users> usersList;


}
