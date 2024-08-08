package com.project.repository.user;

import com.project.domain.concretes.user.User;
import com.project.domain.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Long countAllAdmins(RoleType roleType);
}
