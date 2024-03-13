//package com.emtech.ushurusmart.usermanagement.controller;
//
//import com.emtech.ushurusmart.usermanagement.businessowner.model.BusinessOwner;
//import com.emtech.ushurusmart.usermanagement.businessowner.service.BusinessOwnerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/business-owners")
//public class BusinessOwnerController {
//
//    @Autowired
//    private BusinessOwnerService businessOwnerService;
//
//    @PostMapping("/save")
//    public BusinessOwner createBusinessOwner(@RequestBody BusinessOwner businessOwner) {
//        return ResponseEntity.ok(businessOwnerService.save(businessOwner)).getBody();
//    }
//
//    @GetMapping("/listAll")
//    public ResponseEntity<List<BusinessOwner>> getAllBusinessOwners() {
//        return ResponseEntity.ok(businessOwnerService.findAll());
//    }
//
//    @GetMapping("/business-owner/{BusinessOwnerKRAPin}")
//    public ResponseEntity<Optional<BusinessOwner>> getBusinessOwnerByOwnerKRAPin(@PathVariable String BusinessOwnerKRAPin) {
//        return ResponseEntity.ok(businessOwnerService.findByBusinessOwnerKRAPin(BusinessOwnerKRAPin));
//
//    }
//    @GetMapping("/business/{BusinessKRAPin}")
//    public ResponseEntity<Optional<BusinessOwner>> getBusinessOwnerBusinessKRAPin(@PathVariable String BusinessKRAPin) {
//        return ResponseEntity.ok(businessOwnerService.findByBusinessKRAPin(BusinessKRAPin));
//
//    }
//    @PutMapping("/update/businessOwner")
//    public Optional<BusinessOwner> updateBusinessOwner(@PathVariable String BusinessKRAPin, @RequestBody BusinessOwner businessOwner) {
//        return businessOwnerService.findByBusinessKRAPin(BusinessKRAPin)
//                .map(businessOwner1 -> {
//                    businessOwner.setBusinessOwnerKRAPin(businessOwner.getBusinessOwnerKRAPin());
//                    businessOwner.setBusinessOwnerUsername(businessOwner.getBusinessOwnerUsername());
//                    businessOwner.setEmail(businessOwner.getEmail());
//                    businessOwner.setPassword(businessOwner.getPassword());
//                    businessOwner.setRole(businessOwner.getRole());
//                    businessOwner.setBusinessKRAPin(businessOwner.getBusinessKRAPin());
//                    businessOwner.setPhoneNumber(businessOwner.getPhoneNumber());
//                    return businessOwnerService.save(businessOwner);
//                });
//
//    }
//
//    @DeleteMapping("/deleteByBusinessKRAPin")
//    public ResponseEntity deleteByBusinessKRAPin(@RequestParam String BusinessKRAPin) {
//        return businessOwnerService.deleteByBusinessKRAPin(BusinessKRAPin);
//    }
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
//        businessOwnerService.deleteById(id);
//        return ResponseEntity.ok().build();
//    }
//}
