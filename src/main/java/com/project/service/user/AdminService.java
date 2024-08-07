package com.project.service.user;

import com.project.domain.concretes.user.User;
import com.project.payload.mappers.AdminMapper;
import com.project.payload.mappers.AuthenticationMapper;
import com.project.payload.response.UserResponse;
import com.project.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final AdminMapper adminMapper;


    public List<UserResponse> getAllUsers() {

        List<User> users=userRepository.findAll();

        return users.stream().map(adminMapper::UserToUserResponse).collect(Collectors.toList());

    }
}
