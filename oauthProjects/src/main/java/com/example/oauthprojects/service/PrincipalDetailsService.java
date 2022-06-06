package com.example.oauthprojects.service;

import com.example.oauthprojects.PrincipalDetails;
import com.example.oauthprojects.User;
import com.example.oauthprojects.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailsService implements UserDetailsService {
    @Autowired private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User byUsername = userRepository.findByUsername(username);
        if(byUsername != null){
            return new PrincipalDetails(byUsername);
        }
        return null;
    }
}
