package com.gyowanny.qima.products.service;

import com.gyowanny.qima.products.dto.UserDetailsDTO;
import com.gyowanny.qima.products.entity.User;
import com.gyowanny.qima.products.repository.UserRepository;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userOpt = userRepository.findByUsername(username);
    if (userOpt.isPresent()) {
      log.info("User found {}", userOpt.get().getPassword());
    }
    return userOpt
        .map(user -> UserDetailsDTO.builder().user(user).build())
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
  }
}
