package com.project.service.user;

import com.project.domain.concretes.user.UserRole;
import com.project.domain.enums.RoleType;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.messages.ErrorMessages;
import com.project.repository.user.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRole getUserRoleByRoleType(RoleType roleType)  {
        return  userRoleRepository.findByRoleType(roleType).orElseThrow(
                ()-> new ResourceNotFoundException(ErrorMessages.USER_ROLE_IS_NOT_FOUND)
        );
    }


    public List<UserRole> getAllUserRoles(){
        return userRoleRepository.findAll();
    }

}
