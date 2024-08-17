package com.project.controller.user;

import com.project.payload.request.user.UpdateUsersRequest;
import com.project.payload.request.user.UserPasswordRequest;
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

    @PatchMapping("/auth") //http://localhost:8080/users/auth ++
 //   @PreAuthorize("hasAnyAuthority('ADMIN','TALAS')")
    public ResponseEntity<String> updateUserPassword(@Valid @RequestBody UserPasswordRequest request, HttpServletRequest auth) {

        return userService.updateUserPassword(request, auth);

    }

    @GetMapping("/{id}") //http://localhost:8080/users/1/admin ++
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id, HttpServletRequest request) {
        return userService.getUserById(id, request);
    }

/*    @PatchMapping //http://localhost:8080/users
 //   @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<BaseUserResponse> updateAuthenticatedUser(@Valid @RequestBody UpdateUsersRequest request, HttpServletRequest auth) {

        return userService.updateAuthenticatedUser(request, auth);

    }*/

}
