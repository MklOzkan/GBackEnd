package com.project.service.user;

import com.project.domain.concretes.user.User;
import com.project.domain.enums.RoleType;
import com.project.payload.mappers.AdminMapper;
import com.project.payload.response.user.UserResponse;
import com.project.repository.user.UserRepository;
import com.project.service.helper.MethodHelper;
import com.project.service.helper.PageableHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final AdminMapper adminMapper;
    private final MethodHelper methodHelper;
    private final UserRoleService userRoleService;
    private final PageableHelper pageableHelper;



}
