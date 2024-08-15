package com.project.domain.concretes.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @OneToOne(fetch = FetchType.EAGER)
    private UserRole userRole = new UserRole();


    @Column(unique = true)
    private String password;

    private Boolean builtIn=true;

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    @Column(name = "reset_password_code",unique = true)
//    private String resetCode;


    //    @Column(nullable = false, length = 60)
//    private String firstName;
//
//    @Column(nullable = false, length = 60)
//    private String lastName;
//
//    @Email
//    @Column(unique = true, nullable = false)
//    private String email;


//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH-mm", timezone = "Turkey")
//    @Column(name = "create_at", nullable = false)
//    @Setter(AccessLevel.NONE)
//    private LocalDateTime createdAt;
//
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Turkey")
//    @Column(name = "update_at")
//    @Setter(AccessLevel.NONE)
//    private LocalDateTime updatedAt;




//    @PrePersist
//    private void onCreate() {
//        createdAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    private void onUpdate() {
//        updatedAt=(LocalDateTime.now());
//    }

    //Todo Son 1 haftalik giris listesi eklenecek
}
