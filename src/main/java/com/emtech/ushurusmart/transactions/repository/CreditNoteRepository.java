package com.emtech.ushurusmart.transactions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.emtech.ushurusmart.transactions.entity.CreditNote;

public interface CreditNoteRepository extends JpaRepository<CreditNote, Long> {
}
