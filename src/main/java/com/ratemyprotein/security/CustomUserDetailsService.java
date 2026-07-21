package com.ratemyprotein.security;

import com.ratemyprotein.entity.AppUser;
import com.ratemyprotein.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        AppUser user = userRepository
                .findByEmailIgnoreCase(email.trim())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found"
                ));

        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .disabled(!user.isEnabled())
                .build();
    }
}