package com.project.repository.business.process;

import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.KaliteKontrolStage;
import com.project.domain.concretes.business.process.kalitekontrol.KaliteKontrol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KaliteKontrolRepository extends JpaRepository<KaliteKontrol, Long> {
    @Query("SELECT k FROM KaliteKontrol k WHERE k.productionProcess = ?1 AND k.kaliteKontrolStage = ?2")
    Optional<KaliteKontrol> findByProductionProcess(ProductionProcess productionProcess, KaliteKontrolStage kaliteKontrolStage);

    List<KaliteKontrol> findAllByProductionProcess(ProductionProcess productionProcess);
}
