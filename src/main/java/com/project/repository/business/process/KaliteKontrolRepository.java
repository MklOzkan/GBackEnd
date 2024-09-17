package com.project.repository.business.process;

import com.project.domain.concretes.business.process.kalitekontrol.KaliteKontrol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KaliteKontrolRepository extends JpaRepository<KaliteKontrol, Long> {
}
