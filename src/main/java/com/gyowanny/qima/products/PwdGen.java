package com.gyowanny.qima.products;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PwdGen {

  public static void main(String[] args) {
    System.out.println(new BCryptPasswordEncoder().encode("admin"));
  }
}
