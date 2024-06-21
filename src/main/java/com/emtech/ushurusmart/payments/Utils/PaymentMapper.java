package com.emtech.ushurusmart.payments.Utils;

import org.springframework.stereotype.Component;

import com.emtech.ushurusmart.payments.dtos.PaymentDTO;
import com.emtech.ushurusmart.payments.entity.PaymentEntity;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;

@Component
public class PaymentMapper {

    private final OwnerRepository ownerRepository;

    public PaymentMapper(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public PaymentDTO toDTO(PaymentEntity entity) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(entity.getId());
        dto.setOwnerId(entity.getOwner().getId());
        dto.setBusinessKrapin(entity.getBusiness_krapin());
        dto.setESlipNumber(entity.getESlipNumber());
        dto.setCheckoutRequestID(entity.getCheckoutRequestID());
        dto.setAmount(entity.getAmount());
        dto.setPayerEmail(entity.getPayerEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setMpesaReceipt(entity.getMpesa_receipt());
        dto.setDateInitiated(entity.getDate_initiated());
        dto.setIsPaid(entity.getIsPaid());
        dto.setPaidBy(entity.getPaidBy());
        dto.setTransactionDate(entity.getTransactionDate());
        return dto;
    }

    public PaymentEntity toEntity(PaymentDTO dto) {
        PaymentEntity entity = new PaymentEntity();
        entity.setId(dto.getId());
        entity.setOwner(ownerRepository.findById(dto.getOwnerId()).orElse(null));
        entity.setBusiness_krapin(dto.getBusinessKrapin());
        entity.setESlipNumber(dto.getESlipNumber());
        entity.setCheckoutRequestID(dto.getCheckoutRequestID());
        entity.setAmount(dto.getAmount());
        entity.setPayerEmail(dto.getPayerEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setMpesa_receipt(dto.getMpesaReceipt());
        entity.setDate_initiated(dto.getDateInitiated());
        entity.setIsPaid(dto.getIsPaid());
        entity.setPaidBy(dto.getPaidBy());
        entity.setTransactionDate(dto.getTransactionDate());
        return entity;
    }
}
