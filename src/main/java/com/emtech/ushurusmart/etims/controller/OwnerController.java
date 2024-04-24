package com.emtech.ushurusmart.etims.controller;

import com.emtech.ushurusmart.etims.entity.Etims;

import com.emtech.ushurusmart.etims.service.EtimsOwnerService;
import com.emtech.ushurusmart.utils.controller.ResContructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/etims")
public class OwnerController {

    @Autowired
    private EtimsOwnerService ownerService;

    @PostMapping("/create")
    public ResponseEntity<?> etimsSave(@RequestBody Etims data) {
        ResContructor res = new ResContructor();
        try {
            Etims details = ownerService.save(data);

            res.setMessage("Business Owner added successfully!");
            res.setData(details);
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (Exception e) {
            res.setMessage("Unable to add  Business Owner");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(res);
        }
    }

    @PutMapping("/update/{businessOwnerKRAPin}")
    public ResponseEntity<?> etimsUpdate(@PathVariable String businessOwnerKRAPin, @RequestBody Etims data) {
        ResContructor res = new ResContructor();
        try {
            System.out.println();
            if (ownerService.findByBusinessOwnerKRAPin(businessOwnerKRAPin) == null) {
                res.setMessage("No business owner with those details exists.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
            }
            Etims updated = ownerService.etimsUpdate(data);
            res.setMessage("Business Owner updated Successfully.");
            res.setData(updated);
            return ResponseEntity.ok().body(res);
        } catch (Exception e) {
            res.setMessage("Error while to trying to update details");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }

    }

    @DeleteMapping("/delete/{businessOwnerKRAPin}")
    public ResponseEntity<?> etimsDelete(@PathVariable String businessOwnerKRAPin) {
        ResContructor res = new ResContructor();

        if (ownerService.etimsDelete(businessOwnerKRAPin)) {
            res.setMessage("Business Owner deleted successfully!");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } else {
            res.setMessage("Unable to delete the business owner.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
        }

    }

}
