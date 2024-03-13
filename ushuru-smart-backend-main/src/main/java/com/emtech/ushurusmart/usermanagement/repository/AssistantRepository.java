package com.emtech.ushurusmart.usermanagement.repository;


import java.util.Collection;

import com.emtech.ushurusmart.usermanagement.model.Assistant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssistantRepository extends JpaRepository<Assistant, Long> {

    Collection<Assistant> findByBranch(String branch);

    Assistant findByEmail(String email);
}
