package com.project.domain.concretes.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "t_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.MODULE)
    private Long id;

    @Column(nullable = false,length = 60)
    private String firstName;

    @Column(nullable = false,length = 60)
    private String lastName;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true,nullable = false)
    private String password;

    @Column(unique = true)
    private String resetCode;



}
