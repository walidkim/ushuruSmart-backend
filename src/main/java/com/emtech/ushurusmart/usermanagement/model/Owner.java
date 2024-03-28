package com.emtech.ushurusmart.usermanagement.model;

import com.emtech.ushurusmart.payment.model.PaymentDetails;
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
        @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
        private List<Assistant> assistants;

        @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
        private List<PaymentDetails> payments;




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
