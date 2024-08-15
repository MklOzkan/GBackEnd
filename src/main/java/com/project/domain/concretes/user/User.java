package com.project.domain.concretes.user;
import jakarta.persistence.*;
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

    @Column(unique = true)
    String userName;

    @Column(unique = true)
    private String password;


    private Boolean builtIn=true;

    @OneToOne(fetch = FetchType.EAGER)
    private UserRole userRole=new UserRole() ;




    //Todo Son 1 haftalik giris listesi eklenecek
}
