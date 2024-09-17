package com.project.repository.business.process;

import com.project.domain.concretes.business.process.blokliftmontajamiri.BlokLiftMontaj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlokLiftMontajRepository extends JpaRepository<BlokLiftMontaj, Long> {
}
