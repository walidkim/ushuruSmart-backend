package com.emtech.ushurusmart.etrModule.Repository;

import com.emtech.ushurusmart.etrModule.entity.etr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface etrInvoiceRepo extends JpaRepository <etr, String >{

}
