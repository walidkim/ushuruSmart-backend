package com.emtech.ushurusmart.usermanagement.utils.otp;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "otp_table")
public class otpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "usertag")
    private String usertag;
    @Column(name = "otpcode", nullable = false)
    private String otpcode;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    private String phoneNo; // this is not a column in the database,

    public otpEntity() {
    };

    public String getphoneNo() {
        return phoneNo;
    }
    /// Constructor
    // Getter & setter

    public String getUsertag() {
        return usertag;
    }

    public String getOtpcode() {
        return otpcode;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsertag(String usertag) {
        this.usertag = usertag;
    }

    public void setOtpcode(String otpcode) {
        this.otpcode = otpcode;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "otpEntity [id=" + id + ", usertag=" + usertag + ", otpcode=" + otpcode + ", createdAt=" + createdAt
                + "]";
    }

}