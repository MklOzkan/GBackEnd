package com.project.repository.business.process;

import com.project.domain.concretes.business.process.boyavepaket.BoyaVePaketleme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoyaVePaketlemeRepository extends JpaRepository<BoyaVePaketleme, Long> {
}
