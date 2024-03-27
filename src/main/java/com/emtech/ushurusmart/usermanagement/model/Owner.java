package com.emtech.ushurusmart.usermanagement.model;

import com.emtech.ushurusmart.payment.model.PaymentDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "owners")
public class Owner extends BaseAuth {


        private String businessKRAPin;
        private String businessOwnerKRAPin;
        private String phoneNumber;
//        @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
//        private List<Assistant> assistants;

        @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
        private List<PaymentDetails> payments;

}
