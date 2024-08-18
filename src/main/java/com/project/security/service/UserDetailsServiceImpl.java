package com.project.security.service;


import com.project.domain.concretes.user.User;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.messages.ErrorMessages;
import com.project.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        User user = userRepository.findByUserName(userName).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.USER_IS_NOT_FOUND_BY_USERNAME, userName)));

        if(user != null){
            return new UserDetailsImpl(
                    user.getId(),
                    user.getUserName(),
                    user.getUserRole().getRoleType().name(),
                    user.getPassword(),
                    user.getBuiltIn()
                    );
        }

        throw new UsernameNotFoundException("User not found with userName: " + userName);
    }


}
