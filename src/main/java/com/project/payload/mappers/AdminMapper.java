package com.project.payload.mappers;

import com.project.domain.concretes.user.User;
import com.project.payload.response.SignInResponse;
import com.project.payload.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminMapper {


    public UserResponse UserToUserResponse(User savedUser) {
        return UserResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .builtIn(savedUser.getBuiltIn())
                .createAt(savedUser.getCreatedAt())
                //.updateAt(savedUser.getUpdatedAt())
                //TODO Rolde gelecek buraya
                .build();
    }



}
