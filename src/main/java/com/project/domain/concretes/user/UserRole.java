package com.project.domain.concretes.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.domain.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_userRole")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private String roleName;

}
