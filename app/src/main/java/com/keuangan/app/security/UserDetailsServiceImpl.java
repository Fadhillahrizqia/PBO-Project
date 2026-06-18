package com.keuangan.app.security;

import com.keuangan.app.model.User;              // ← update
import com.keuangan.app.repository.UserRepository; // ← update
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
@Transactional
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username)
        .orElseThrow(() ->
            new UsernameNotFoundException("User tidak ditemukan: " + username)
        );
    return UserDetailsImpl.build(user);
}
}