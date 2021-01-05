package br.com.springessentials2.demo.service;

import br.com.springessentials2.demo.repository.DevDojoUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DevDojoUserDetailsService implements UserDetailsService {
    private final DevDojoUserRepository devDojoUserRepository;

    @Override
    public UserDetails loadUserByUsername(String user) {
        return Optional.of(devDojoUserRepository.findByUserName(user))
                .orElseThrow(()-> new UsernameNotFoundException("User not found."));
    }
}
