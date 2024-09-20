package com.project.repository.business.process;

import com.project.domain.concretes.business.process._enums.TalasliOperationType;
import com.project.domain.concretes.business.process.talasliimalatamiri.TalasliImalat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TalasliImalatRepository extends JpaRepository<TalasliImalat, Long> {

    Optional<TalasliImalat> findFirstByOperationType(TalasliOperationType operationType);


}
