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
@Table(name = "owners")
public class Owner extends BaseAuth {
        private String KRAPin;
        
        private Integer phoneNumber;

        @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
        private List<Assistant> usersList;

        @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
        private List<PaymentDetails> payments;

}
