package com.project.payload.mappers;

import com.project.domain.concretes.user.User;
import com.project.domain.concretes.user.UserRole;
import com.project.payload.request.abstracts.AbstractUserRequest;
import com.project.payload.response.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AdminMapper {


    public UserResponse userToUserResponse(User savedUser) {
        return UserResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .builtIn(savedUser.getBuiltIn())
                .createAt(savedUser.getCreatedAt())
                .updateAt(savedUser.getUpdatedAt())
                .userRole(savedUser.getUserRole().stream().map(UserRole::getRoleName).collect(Collectors.toSet()))
                .build();
    }

    public User userToUser(AbstractUserRequest request,User user) {
        return user.toBuilder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
    }



}
