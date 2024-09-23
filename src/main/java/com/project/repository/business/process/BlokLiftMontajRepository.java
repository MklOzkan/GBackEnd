package com.project.repository.business.process;

import com.project.domain.concretes.business.process._enums.BlokLiftOperationType;
import com.project.domain.concretes.business.process.blokliftmontajamiri.BlokLiftMontaj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlokLiftMontajRepository extends JpaRepository<BlokLiftMontaj, Long> {
    Optional<BlokLiftMontaj> findFirstByOperationType(BlokLiftOperationType operationType);
}
