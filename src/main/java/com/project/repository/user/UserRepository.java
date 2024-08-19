package com.project.repository.user;

import com.project.domain.concretes.user.User;
import com.project.domain.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {


  
    @Query("SELECT COUNT(u) FROM User u WHERE :roleType IN (SELECT r.roleType FROM u.userRole r)")
    Long countAllAdmins(@Param("roleType") RoleType roleType);


    Optional<User> findByPassword(String password);

    Optional<User> findByUserRoleRoleType(RoleType roleType);

    Optional<User> findByUserRoleRoleName(String roleByRequest);

    Optional<User> findByUsername(String username);


    boolean existsByUserRoleRoleName(String role);
}

