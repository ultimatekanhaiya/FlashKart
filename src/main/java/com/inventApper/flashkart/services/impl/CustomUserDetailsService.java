package com.inventApper.flashkart.services.impl;

import com.inventApper.flashkart.entities.User;
import com.inventApper.flashkart.exceptions.ResourceNotFoundException;
import com.inventApper.flashkart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User", "email", username));
        return user;
    }
}
