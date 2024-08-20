package com.project.security.service;


import com.project.domain.concretes.user.User;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.messages.ErrorMessages;
import com.project.repository.user.UserRepository;
import com.project.service.helper.MethodHelper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MethodHelper methodHelper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        User user = methodHelper.loadUserByUsername(userName);

            return new UserDetailsImpl(
                    user.getId(),
                    user.getUsername(),
                    user.getUserRole().getRoleType().getName(),
                    user.getPassword(),
                    user.getBuiltIn()
                    );
    }


}
