package com.project.payload.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgetPasswordRequest {

    @NotBlank(message = "Please enter your email")
    @Email(message = "Please enter valid email")
    @Size(min = 10, max = 80, message = "Your email '${validatedValue}' should be between {min} and {max} chars")
    private String email;

}
