package com.example.demo.service;

import com.example.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = repo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), Collections.emptyList());
    }
}
