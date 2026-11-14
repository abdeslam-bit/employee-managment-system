package com.myFullstack.employee_management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.myFullstack.employee_management.repositories.UserRequestRepo;


@Service 
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired 
    private UserRequestRepo userRequestRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRequestRepo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
    
}
