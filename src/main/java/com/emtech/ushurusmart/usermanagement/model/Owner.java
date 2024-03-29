package com.emtech.ushurusmart.usermanagement.model;

import com.emtech.ushurusmart.payment.model.PaymentDetails;
import com.emtech.ushurusmart.transactions.entity.Product;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
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
        @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        private List<Assistant> assistants;
        @JsonManagedReference
        @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        private List<PaymentDetails> paymentDetails;


        @JsonManagedReference
        @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch =  FetchType.EAGER)
        private List<Product> products;


        public void addProduct(Product prod){
                this.products.add(prod);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return getRole().getAuthorities();
        }



        @Override
        public String getUsername() {
                return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
                return true;
        }

        @Override
        public boolean isAccountNonLocked() {
                return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
                return false;
        }

        @Override
        public boolean isEnabled() {
                return true;
        }
}
