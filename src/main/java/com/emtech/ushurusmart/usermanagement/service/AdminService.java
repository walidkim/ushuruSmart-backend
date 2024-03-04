package com.emtech.ushurusmart.usermanagement.service;

import com.emtech.ushurusmart.usermanagement.model.Admin;
import com.emtech.ushurusmart.usermanagement.repository.AdminRepository;

import jakarta.transaction.Transactional;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private AdminRepository adminRepository;
    @Autowired
    public Admin admin;

    public Admin getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    @SuppressWarnings("null")
    @Transactional
    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    @Transactional
    public Admin updateAdmin(Long id, Admin updatedAdmin) {
        Optional<Admin> adminOptional = adminRepository.findById(id);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            admin.setUsername(updatedAdmin.getUsername());
            admin.setPassword(updatedAdmin.getPassword());
            admin.setPhonenumber(updatedAdmin.getPhonenumber());
            admin.setKRAPin(updatedAdmin.getKRAPin());
            return adminRepository.save(admin);
        }
        return null;

    }

    @Transactional
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    public Object findByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEmail'");
    }

    public void save(Admin landlord) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }
}
