package com.emtech.ushurusmart.usermanagement.controller;

import com.emtech.ushurusmart.usermanagement.Dtos.ResContructor;
import com.emtech.ushurusmart.usermanagement.model.Etims;
import com.emtech.ushurusmart.usermanagement.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/admin")
public class AdminController {

        private final AdminService adminService;

        public AdminController(AdminService adminService) {
            this.adminService = adminService;
        }

        @PostMapping("/etims")
        public ResponseEntity<?> etimsSave(@RequestBody Etims data) {

            adminService.etimsSave(data);

            ResContructor res = new ResContructor();
            res.setMessage("Business Owner added successfully!");

            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }

        @PutMapping("/etims/{businessOwnerKRAPin}")
        public ResponseEntity<?> etimsUpdate(@PathVariable String businessOwnerKRAPin, @RequestBody Etims data) {

            //first find the details of businessOwner kRa pin . The data  does not contain the id of the business owner
            adminService.etimsUpdate(data);

            ResContructor res = new ResContructor();
            res.setMessage("Business Owner updated successfully!");

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
        }

        @DeleteMapping("/etims/{businessOwnerKRAPin}")
        public ResponseEntity<?> etimsDelete(@PathVariable String businessOwnerKRAPin) {

                adminService.etimsDelete(businessOwnerKRAPin);

                ResContructor res = new ResContructor();
                res.setMessage("Business Owner deleted successfully!");

                return  ResponseEntity.status(HttpStatus.OK).body(res);
        }

}
