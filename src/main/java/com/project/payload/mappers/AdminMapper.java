package com.project.payload.mappers;

import com.project.domain.concretes.user.User;
import com.project.domain.concretes.user.UserRole;
import com.project.payload.request.abstracts.AbstractUserRequest;
import com.project.payload.response.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AdminMapper {


    public UserResponse userToUserResponse(User savedUser) {
        return UserResponse.builder()
                .id(savedUser.getId())
                .builtIn(savedUser.getBuiltIn())
                .userRole(savedUser.getUserRole().getRoleName())
                .build();
    }







}
