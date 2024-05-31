package com.emtech.ushurusmart.usermanagement.repository;

import com.emtech.ushurusmart.usermanagement.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
}
