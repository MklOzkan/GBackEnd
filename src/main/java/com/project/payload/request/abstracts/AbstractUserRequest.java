package com.project.payload.request.abstracts;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractUserRequest {

    @NotBlank
    @Size(min = 2, max = 30, message = "First name '${validatedValue}' must be between {min} and {max}")
    @Pattern(regexp = "^[a-zA-ZçÇğĞıİöÖşŞüÜ]+$", message = "Your first name must be consist of the characters a-z")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 30, message = "Last name '${validatedValue}' must be between {min} and {max} long")
    @Pattern(regexp = "^[a-zA-ZçÇğĞıİöÖşŞüÜ]+$", message = "Your last name must be consist of the characters a-z")
    private String lastName;


    @NotBlank(message = "Please enter your email")
    @Email(message = "Please enter valid email")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
    @Size(min = 10, max = 80, message = "Your email '${validatedValue}' should be between {min} and {max} chars")
    private String email;









}
