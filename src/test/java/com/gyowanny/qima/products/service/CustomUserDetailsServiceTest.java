package com.gyowanny.qima.products.service;

import com.gyowanny.qima.products.dto.UserDetailsDTO;
import com.gyowanny.qima.products.entity.User;
import com.gyowanny.qima.products.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

  private UserRepository userRepository;
  private CustomUserDetailsService userDetailsService;

  @BeforeEach
  void setup() {
    userRepository = mock(UserRepository.class);
    userDetailsService = new CustomUserDetailsService(userRepository);
  }

  @Test
  void shouldReturnUserDetailsWhenUserExists() {
    // Given
    User user = new User(1L, "admin", "$2a$10$abc123", "ADMIN");
    when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

    // When
    UserDetails result = userDetailsService.loadUserByUsername("admin");

    // Then
    assertThat(result).isInstanceOf(UserDetailsDTO.class);
    assertThat(result.getUsername()).isEqualTo("admin");
    assertThat(result.getPassword()).isEqualTo("$2a$10$abc123");
    assertThat(result.getAuthorities()).extracting("authority").containsExactly("ROLE_ADMIN");
  }

  @Test
  void shouldThrowExceptionWhenUserNotFound() {
    // Given
    when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

    // When / Then
    assertThatThrownBy(() -> userDetailsService.loadUserByUsername("ghost"))
        .isInstanceOf(UsernameNotFoundException.class)
        .hasMessageContaining("User not found: ghost");
  }
}