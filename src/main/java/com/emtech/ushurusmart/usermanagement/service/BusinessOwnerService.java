//package com.emtech.ushurusmart.usermanagement.service;
//
//import com.emtech.ushurusmart.usermanagement.businessowner.model.BusinessOwner;
//import com.emtech.ushurusmart.usermanagement.businessowner.repository.BusinessOwnerRepository;
//import com.emtech.ushurusmart.usermanagement.model.Admin;
//import io.swagger.v3.oas.models.responses.ApiResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Slf4j
//public class BusinessOwnerService {
//
//    private final BusinessOwnerRepository businessOwnerRepository;
//
//    public BusinessOwnerService(BusinessOwnerRepository repository, BusinessOwnerRepository businessOwnerRepository, BusinessOwner businessOwner) {
//        this.businessOwnerRepository = businessOwnerRepository;
//    }
//
//    public BusinessOwner save(BusinessOwner businessOwner){
//        return businessOwnerRepository.save(businessOwner);
//    }
//
//    public void deleteById(Integer id) {
//        businessOwnerRepository.deleteById(id);
//    }
//    public ApiResponse<?> businessOwnerDelete(Long id) {
//        try {
//            Admin currentUser = SecurityUtils.getCurrentUser();
//            if (currentUser == null) {
//                return new ApiResponse<>("Access denied. Admin is not authenticated.", null, HttpStatus.UNAUTHORIZED.value());
//            }
//
//            Optional<BusinessOwner> businessOwnerOptional = businessOwnerRepository.findById(id)
//                    ;
//
//            if (businessOwnerOptional.isPresent()) {
//                BusinessOwner businessOwner = businessOwnerOptional.get();
//
//                // Perform soft delete by updating flags and timestamps
//                businessOwner.setDeletedAt(LocalDateTime.now());
//                businessOwner.setDeletedBy(currentUser);
//                businessOwner.setDeletedFlag(Constants.YES);
//
//                // Additional logic for related entities can be added here
//
//                // Save the changes
//                businessOwnerRepository.save(businessOwner);
//
//                return new ApiResponse<>("Business Owner was successfully deleted.", null, HttpStatus.NO_CONTENT.value());
//            } else {
//                return new ApiResponse<>("Business Owner not found.", null, HttpStatus.NOT_FOUND.value());
//            }
//        } catch (Exception e) {
//            log.error("An error occurred while deleting Business Owner.", e);
//            return new ApiResponse<>("An error occurred while deleting Business Owner.", null, HttpStatus.INTERNAL_SERVER_ERROR.value());
//        }
//    }
//    public ResponseEntity deleteByBusinessKRAPin(String BusinessKRAPin) {
//        businessOwnerRepository.deleteByBusinessKRAPin(BusinessKRAPin);
//        return null;
//    }
//
//    public List<BusinessOwner> findAll() {
//        return businessOwnerRepository.findAll();
//    }
//
//    public Optional<BusinessOwner> findByBusinessOwnerKRAPin(String BusinessOwnerKRAPin) {
//        return businessOwnerRepository.findByBusinessOwnerKRAPin(BusinessOwnerKRAPin);
//    }
//    public Optional<BusinessOwner> findByBusinessKRAPin(String BusinessKRAPin) {
//        return businessOwnerRepository.findByBusinessKRAPin(BusinessKRAPin);
//    }
//}
