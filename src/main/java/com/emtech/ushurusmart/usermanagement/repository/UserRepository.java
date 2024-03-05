package com.emtech.ushurusmart.usermanagement.repository;

import com.emtech.ushurusmart.usermanagement.model.User;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Collection<User> findByBranch(String branch);

    User findByEmail(String email);
}
