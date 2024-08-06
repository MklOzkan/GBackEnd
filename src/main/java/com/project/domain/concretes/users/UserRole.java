package com.project.domain.concretes.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.domain.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "t_userRole")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.MODULE)
    private Long id;


    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private RoleType roleType;

    private String roleName;

    @ManyToMany(mappedBy = "userRole")
    @JsonIgnore
    private Set<User> user = new HashSet<>();

}
