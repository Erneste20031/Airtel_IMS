package com.airtel.inventory.service;

import com.airtel.inventory.model.User;
import com.airtel.inventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("===== Attempting to login with username: " + username + " =====");
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("===== User NOT found: " + username + " =====");
                    return new UsernameNotFoundException("User not found: " + username);
                });
        
        System.out.println("===== User FOUND: " + user.getUsername() + " =====");
        System.out.println("===== Password in DB: " + user.getPassword() + " =====");
        System.out.println("===== Is Active: " + user.isActive() + " =====");
        
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.isActive(),
            true, true, true,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()))
        );
    }
}