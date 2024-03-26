package com.emtech.ushurusmart.usermanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "owners")
public class Owner extends BaseAuth  {

        @Column(name = "business_kra_pin")
        private String businessKraPin;

        @Column(name = "business_owner_kra_pin")
        private String businessOwnerKraPin;

        @Column(name = "phone_number")
        private Integer phoneNumber;


//        @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
//        private List<Assistant> assistants;
//
//        @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
//        private List<PaymentDetails> payments;

}
