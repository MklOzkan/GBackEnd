package com.project.repository.business.process;

import com.project.domain.concretes.business.process.ProductionProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionProcessRepository extends JpaRepository<ProductionProcess, Long> {
}
