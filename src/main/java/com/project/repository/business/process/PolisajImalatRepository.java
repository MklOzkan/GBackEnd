package com.project.repository.business.process;

import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process.polisajamiri.PolisajImalat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PolisajImalatRepository extends JpaRepository<PolisajImalat, Long> {
    Optional<PolisajImalat> findPolisajImalatByProductionProcess(ProductionProcess productionProcess);
}
