package com.emtech.ushurusmart.usermanagement.repository;


import com.emtech.ushurusmart.usermanagement.model.Assistant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface AssistantRepository extends JpaRepository<Assistant, Long> {

    Collection<Assistant> findByBranch(String branch);

    Assistant findByEmail(String email);

    Assistant findByPhoneNumber(String phoneNumber);

    @Query("SELECT a FROM Assistant a WHERE a.ownerId = :ownerId")
    Assistant getOwnerId(@Param("ownerId") long ownerId);


}
