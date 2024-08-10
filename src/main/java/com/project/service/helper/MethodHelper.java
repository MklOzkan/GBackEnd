package com.project.service.helper;

import com.project.domain.concretes.user.User;
import com.project.domain.concretes.user.UserRole;
import com.project.domain.enums.RoleType;
import com.project.exception.BadRequestException;
import com.project.exception.ConflictException;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.request.abstracts.AbstractUserRequest;
import com.project.repository.user.UserRepository;
import com.project.service.user.UserRoleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MethodHelper {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;

    public void checkDuplicate(String email) {

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException(String.format(ErrorMessages.THIS_PHONE_NUMBER_IS_ALREADY_TAKEN, email));
        }
    }

    public User findByUserByEmail(String email){

        if (email == null || email.isEmpty()) {
            throw new ResourceNotFoundException("Email can not be null or empty");
        }

        return userRepository.findByEmail(email).orElseThrow(()-> new BadRequestException(String.format(ErrorMessages.THERE_IS_NO_USER_WITH_THIS_EMAIL,email)));
    }

    public User findUserWithId(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.USER_ID_IS_NOT_FOUND, id)));
    }


    public User getUserByHttpRequest(HttpServletRequest request) {
        return findByUserByEmail(getEmailByRequest(request));

    }

    public String getEmailByRequest(HttpServletRequest request) {
        return (String) request.getAttribute("email");
    }

    public boolean isBuiltIn(User user) {

        return user.getBuiltIn();
    }

    public void checkRoles(User user, RoleType... roleTypes) {

        Set<RoleType> roles = new HashSet<>();
        Collections.addAll(roles, roleTypes);

        for (UserRole userRole : user.getUserRole()) {
            if (roles.contains(userRole.getRoleType())) return;
        }
        throw new ResourceNotFoundException(ErrorMessages.USER_ROLE_IS_NOT_FOUND);
    }


    public void checkUniqueProperties(User user, AbstractUserRequest request) {

        boolean changed = false;
        String changedEmail = "";


        if (!user.getEmail().equalsIgnoreCase(request.getEmail())) {
            changed = true;
            changedEmail = request.getEmail();
        }
        if (changed) {
            checkDuplicate(changedEmail);
        }
    }

    public Set<UserRole>stringRoleToUserRole(Set<String>role){
        Set<UserRole> userRoleSet = new HashSet<>();
        try {
        for (String rl:role) {
            RoleType roleType = RoleType.fromString(rl);
            UserRole userRole = userRoleService.getUserRoleByRoleType(roleType);
            userRoleSet.add(userRole);
        }

        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(ErrorMessages.USER_ROLE_IS_NOT_FOUND);
        }
     return userRoleSet;
    }


}
