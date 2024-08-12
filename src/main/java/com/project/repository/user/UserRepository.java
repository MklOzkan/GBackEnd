package com.project.repository.user;

import com.project.domain.concretes.user.User;
import com.project.domain.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u WHERE :roleType IN (SELECT r.roleType FROM u.userRole r)")
    Long countAllAdmins(@Param("roleType") RoleType roleType);




    @Query("SELECT u FROM User u WHERE " +
            "(:firstName IS NULL OR u.firstName LIKE %:firstName%) AND " +
            "(:lastName IS NULL OR u.lastName LIKE %:lastName%) AND " +
            "(:email IS NULL OR u.email LIKE %:email%)")
    Page<User> findAll(@Param("firstName") String firstName,
                       @Param("lastName") String lastName,
                       @Param("email") String email,
                       Pageable pageable);



    Optional<User> findByResetCode(String code);

    boolean existsByResetCode(String resetCode);
}
