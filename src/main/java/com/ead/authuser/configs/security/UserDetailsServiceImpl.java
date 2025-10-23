package com.ead.authuser.configs.security;

import com.ead.authuser.exceptions.NotFoundException;
import com.ead.authuser.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(UserDetailsImpl::build)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
    }

    public UserDetails loadUserById(UUID uuid) {
        return userRepository.findByUserId(uuid)
                .map(UserDetailsImpl::build)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + uuid));
    }
}
