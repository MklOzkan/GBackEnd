package com.project.payload.response;



import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)

public class SignInResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

}
