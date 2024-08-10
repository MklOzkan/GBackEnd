package com.project.payload.request;

import com.project.payload.request.abstracts.BaseUserRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserUpdateByAdminRequest extends BaseUserRequest {


    @NotBlank
    private Set<String>roles=new HashSet<>();

}
