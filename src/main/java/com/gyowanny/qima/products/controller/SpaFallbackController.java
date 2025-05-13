package com.gyowanny.qima.products.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaFallbackController {

  @RequestMapping(value = { "/", "/login", "/products", "/categories" })
  public String forward() {
    return "forward:/index.html";
  }
}