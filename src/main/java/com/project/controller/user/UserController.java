package com.project.controller.user;

import com.project.payload.request.user.UpdateUsersRequest;
import com.project.payload.request.user.UserPasswordRequest;
import com.project.payload.request.user.UserRequest;
import com.project.payload.response.abstracts.BaseUserResponse;
import com.project.payload.response.user.UserResponse;
import com.project.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

private final UserService userService;

    @PreAuthorize("hasAnyAuthority('Admin')")
    @PostMapping("/save/{userRole}")
    public ResponseEntity<String>saveUser(
            @RequestBody @Valid UserRequest userRequest,
            @PathVariable String userRole) {
        return userService.saveUser(userRequest,userRole);
    }

}
