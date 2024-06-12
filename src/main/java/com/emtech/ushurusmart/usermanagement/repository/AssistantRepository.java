package com.emtech.ushurusmart.usermanagement.repository;


import com.emtech.ushurusmart.usermanagement.model.Assistant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface AssistantRepository extends JpaRepository<Assistant, Long> {

    Collection<Assistant> findByBranch(String branch);

    Assistant findByEmail(String email);

    Assistant findByPhoneNumber(String phoneNumber);

    int countByLoggedInStatus(boolean b);

    // add query to list assistants by owner id

//    @Query("SELECT COUNT(u) FROM Assistant u WHERE u.lastLogin > :lastLogin")
//    int countByLastLoginAfter(LocalDateTime lastLogin);
}
