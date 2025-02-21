package com.api_gateway.UserService;

import com.api_gateway.Entity.User;
import com.api_gateway.Exception.UsernameNotFound;
import com.api_gateway.Repository.UserRepository;
import com.api_gateway.UserDetails.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserService implements UserDetailsService {


    private final UserRepository userRepository;

    @Autowired
    public CustomUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFound("User not found");
        }
        return new CustomUserDetails(user);
    }
}
