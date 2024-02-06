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
            columnDefinition = "TEXT"
    )
    private String fullName;
    private String password;
    private Integer phoneNumber;

    public Admin(String KRAPin, String businessName, String fullName, String password, Integer phoneNumber) {
        this.KRAPin = KRAPin;
        this.businessName = businessName;
        this.fullName = fullName;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
    @OneToMany(mappedBy = "admin",cascade = CascadeType.ALL)
    private List<Users> usersList;

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", KRAPin='" + KRAPin + '\'' +
                ", businessName='" + businessName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}
