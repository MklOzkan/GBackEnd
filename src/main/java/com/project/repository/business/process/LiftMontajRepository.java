package com.project.repository.business.process;

import com.project.domain.concretes.business.process.liftmontajamiri.LiftMontaj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiftMontajRepository extends JpaRepository<LiftMontaj, Long> {
}
