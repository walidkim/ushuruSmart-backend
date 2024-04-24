package com.emtech.ushurusmart.usermanagement.TrackingEntity;

import com.emtech.ushurusmart.usermanagement.utils.Constant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.catalina.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class TrackingEntity {
    @JsonIgnore
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by")
    private User addedBy;

    @JsonIgnore
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @JsonIgnore
    @Column(nullable = false)
    private String updateFlag= Constant.NO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;



    @JsonIgnore
    @Column(nullable = false)
    private String deletedFlag=Constant.NO;
    @JsonIgnore
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by")
    private User deletedBy;


    @PreRemove
    protected void onDelete(){
        deletedAt=LocalDateTime.now();
        deletedFlag=Constant.YES;
        deletedBy=getCurrentUser();
    }


    @PrePersist
    protected void onCreate() {
        // createdAt = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        addedBy=getCurrentUser();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        updateFlag=Constant.YES;
        updatedBy = getCurrentUser();
    }


    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            if (authentication.getPrincipal() instanceof User) {
                return (User) authentication.getPrincipal();
            }
        }

        return null;
    }


}

