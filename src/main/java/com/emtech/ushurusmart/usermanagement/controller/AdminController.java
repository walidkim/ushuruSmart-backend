package com.emtech.ushurusmart.usermanagement.controller;


import com.emtech.ushurusmart.usermanagement.Dtos.ResConstructor;
import com.emtech.ushurusmart.usermanagement.model.Etims;
import com.emtech.ushurusmart.usermanagement.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/admin")
@Tag(name = "Admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/etims")
    public ResponseEntity<?> etimsSave(@RequestBody Etims data) {
        ResConstructor res = new  ResConstructor();
        try {
            Etims details = adminService.etimsSave(data);

            res.setMessage("Business Owner added successfully!");
            res.setData(details);
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (Exception e) {
            res.setMessage("Unable to add  Business Owner");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(res);
        }
    }

    @PutMapping("/etims/{businessOwnerKRAPin}")
    public ResponseEntity<?> etimsUpdate(@PathVariable String businessOwnerKRAPin, @RequestBody Etims data) {
        ResConstructor res = new  ResConstructor();
        try {
            if (adminService.findByBusinessOwnerKRAPin(businessOwnerKRAPin) == null) {
                res.setMessage("No business owner with those details exists.");
                return ResponseEntity.ok().body(res);
            }
            Etims updated = adminService.etimsUpdate(data);
            res.setMessage("Business Owner updated Successfully.");
            res.setData(updated);
            return ResponseEntity.ok().body(res);
        } catch (Exception e) {
            res.setMessage("Error while to trying to update details");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }

    }

    @DeleteMapping("/etims/{businessOwnerKRAPin}")
    public ResponseEntity<?> etimsDelete(@PathVariable String businessOwnerKRAPin) {
        ResConstructor res = new  ResConstructor();

        if (adminService.etimsDelete(businessOwnerKRAPin)) {
            res.setMessage("Business Owner deleted successfully!");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } else {
            res.setMessage("Unable to delete the business owner.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
        }

    }
    @GetMapping("/find/by/business/{kra}")
    ResponseEntity<?> findBusinessKra(@PathVariable String kra) {
            Optional<Etims> findkra = Optional.ofNullable(adminService.findByBusinessOwnerKRAPin(kra));
        System.out.println("fetching kra...."+ findkra);
        Etims response = new Etims();
            if (findkra != null){
                 response = findkra.get();
            }
        return ResponseEntity.status(HttpStatus.OK).body(response);
        }

}
