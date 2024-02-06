package com.emtech.ushurusmart.usermanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.emtech.ushurusmart.payment.model.PaymentDetails;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(uniqueConstraints = {
                @UniqueConstraint(name = "kra_unique", columnNames = "KRAPin")
})
public class Admin {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_sequence")
        @Column(nullable = false)
        private Long id;
        @Column(nullable = false)
        private String KRAPin;
        @Column(nullable = false)
        private String username;
        @Column(nullable = false)
        private String password;

        @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
        private List<Users> usersList;

        @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
        private List<PaymentDetails> payments;

}
