package com.project.service.user;

import com.project.domain.concretes.user.User;
import com.project.payload.mappers.AdminMapper;
import com.project.payload.mappers.AuthenticationMapper;
import com.project.payload.mappers.UserMapper;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.user.UpdatePasswordRequest;
import com.project.payload.request.user.UserRequest;
import com.project.repository.user.UserRepository;
import com.project.service.helper.MethodHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MethodHelper methodHelper;
    private final AdminMapper adminMapper;
    private final UserMapper userMapper;
    private final AuthenticationMapper authenticationMapper;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<String> saveUser(UserRequest userRequest, String userRole) {
        User user = userMapper.mapUserRequestToUser(userRequest, userRole);
        userRepository.save(user);
        return ResponseEntity.ok("User created successfully");
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void updatePassword(String employeeName, UpdatePasswordRequest updatePasswordRequest, HttpServletRequest request) {

        String admin =  (String) request.getAttribute("username");
        User user = methodHelper.findUserByUsername(admin);
        methodHelper.isAdmin(user);
        User userToUpdate = methodHelper.findUserByUsername(employeeName);
        userToUpdate.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(userToUpdate);
    }
}
