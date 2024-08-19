package com.project.payload.mappers;

import com.project.domain.concretes.user.User;
import com.project.domain.concretes.user.UserRole;
import com.project.domain.enums.RoleType;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.request.abstracts.BaseUserRequest;
import com.project.payload.response.user.UserResponse;
import com.project.service.user.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final UserRoleService userRoleService;

    private final PasswordEncoder passwordEncoder;

    public User mapUserRequestToUser(BaseUserRequest userRequest, String userRole) {
        User user = User.builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build();
        if (userRole.equalsIgnoreCase(RoleType.ADMIN.getName())) {
            if (Objects.equals(userRequest.getUsername(), "Admin")) {
                user.setBuiltIn(true);
            }
            user.setUserRole(userRoleService.getUserRole(RoleType.ADMIN));
        }else if (userRole.equalsIgnoreCase("Employee")) {
            user.setUserRole(userRoleService.getUserRole(RoleType.EMPLOYEE));
        }else {
            throw new ResourceNotFoundException(
                    String.format(ErrorMessages.USER_ROLE_IS_NOT_FOUND, userRole));
        }
        return user;
    }

    public UserResponse mapUserToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .userRole(user.getUserRole().getRoleType().getName())
                .builtIn(user.getBuiltIn())
                .build();
    }
}
