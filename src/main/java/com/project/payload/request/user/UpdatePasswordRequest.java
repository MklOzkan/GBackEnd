package com.project.payload.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePasswordRequest {

    @NotBlank(message = "Enter a valid Password")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=*/_)!.,(§<>'-]).*$",
            message = "Password must contain at least one digit, one lowercase character, one uppercase character, and one special character (@#$%^&+=)")
    private String newPassword;

}
