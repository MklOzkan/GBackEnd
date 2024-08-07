package com.project.payload.mappers;

import com.project.domain.concretes.user.User;
import com.project.payload.request.SignInRequest;
import com.project.payload.response.SignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationMapper {


    public User SignInRequestToUser(SignInRequest signInRequest) {
      return   User.builder().firstName(signInRequest.getFirstName())
                .lastName(signInRequest.getLastName())
                .email(signInRequest.getEmail())
                .build();
    }

    public SignInResponse UserToSignInResponse(User savedUser) {
        return SignInResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .build();
    }
}
