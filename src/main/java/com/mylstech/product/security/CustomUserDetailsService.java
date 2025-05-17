package com.mylstech.product.security;

import com.mylstech.product.model.User;
import com.mylstech.product.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail ( email )
                .orElseThrow ( () -> new UsernameNotFoundException ( "User not found with email: " + email ) );

        return new UserSecurityDetails ( user );
    }
}