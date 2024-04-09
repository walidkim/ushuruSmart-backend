package com.emtech.ushurusmart.utils.otp;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "otp_table")
public class OtpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String userTag="";
    @Column(nullable = false)
    private String otpCode="";
    private LocalDateTime validUntil;

}