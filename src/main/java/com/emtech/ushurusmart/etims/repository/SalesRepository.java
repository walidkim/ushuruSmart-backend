package com.emtech.ushurusmart.etims.repository;

import com.emtech.ushurusmart.etims.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesRepository  extends JpaRepository<Sale, Long> {
}
