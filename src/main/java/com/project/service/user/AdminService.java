package com.project.service.user;

import com.project.domain.concretes.user.User;
import com.project.domain.concretes.user.UserRole;
import com.project.domain.enums.RoleType;
import com.project.exception.BadRequestException;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.mappers.AdminMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.UserUpdateByAdminRequest;
import com.project.payload.response.UserResponse;
import com.project.repository.user.UserRepository;
import com.project.service.helper.MethodHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final AdminMapper adminMapper;
    private final MethodHelper methodHelper;
    private final UserRoleService userRoleService;


    public List<UserResponse> getAllUsers(HttpServletRequest request) {

        User user = methodHelper.getUserByHttpRequest(request);
        methodHelper.checkRoles(user, RoleType.ADMIN);

        List<User> users = userRepository.findAll();

        return users.stream().map(adminMapper::userToUserResponse).collect(Collectors.toList());

    }

    public ResponseEntity<UserResponse> getUserByEmail(HttpServletRequest request, String email) {

        User user = methodHelper.getUserByHttpRequest(request);
        methodHelper.checkRoles(user, RoleType.ADMIN);

        return ResponseEntity.ok(adminMapper.userToUserResponse(methodHelper.findByUserByEmail(email)));


    }

    public ResponseEntity<String> setUserRole(HttpServletRequest request, Set<String> role, String email) {

        User authenticatedUser = methodHelper.getUserByHttpRequest(request);
        methodHelper.checkRoles(authenticatedUser, RoleType.ADMIN);
        User user = methodHelper.findByUserByEmail(email);

        user.setUserRole(methodHelper.stringRoleToUserRole(role));
        userRepository.save(user);

        return new ResponseEntity<>(SuccessMessages.THE_ROLES_HAS_ADDED, HttpStatus.CREATED);
    }

    public Long countAllAdmins() {

        return userRepository.countAllAdmins(RoleType.ADMIN);

    }

    public ResponseEntity<UserResponse> updateUserByAdmin(Long id, UserUpdateByAdminRequest adminRequest, HttpServletRequest request) {
        User user = methodHelper.getUserByHttpRequest(request);
        methodHelper.checkRoles(user, RoleType.ADMIN);
        User fetchedUser = methodHelper.findUserWithId(id);
        methodHelper.checkUniqueProperties(fetchedUser, adminRequest);
        Set<UserRole> userRoles = methodHelper.stringRoleToUserRole(adminRequest.getRoles());
        fetchedUser.setUserRole(userRoles);
        User mappedUser = adminMapper.userToUser(adminRequest, fetchedUser);
        return ResponseEntity.ok(adminMapper.userToUserResponse(userRepository.save(mappedUser)));
    }

    public String deleteUserByAdmin(Long id, HttpServletRequest request) {
        User user = methodHelper.getUserByHttpRequest(request);
        methodHelper.checkRoles(user, RoleType.ADMIN);
        User deleteUser = methodHelper.findUserWithId(id);
        if (deleteUser.getBuiltIn()) throw new BadRequestException(ErrorMessages.BUILTIN_USER_CAN_NOT_BE_DELETED);
        if (user.getBuiltIn()) {
            userRepository.delete(deleteUser);
        } else {
            boolean isUserAdmin = deleteUser.getUserRole().stream()
                    .anyMatch(userRole -> RoleType.ADMIN.equals(userRole.getRoleType()));
            if (isUserAdmin) {
                throw new BadRequestException(ErrorMessages.ADMIN_CANNOT_DELETE_ADMIN);
            }
            userRepository.delete(deleteUser);
        }
        return SuccessMessages.THE_USER_HAS_BEEN_DELETED;
    }
}
