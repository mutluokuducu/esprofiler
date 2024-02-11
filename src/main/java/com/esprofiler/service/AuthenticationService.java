package com.esprofiler.service;

import java.util.Map;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {
  Map<String, Object> getUserDetails(Authentication authentication);

  boolean verifyVendorVID(Authentication authentication, String expectedVID);

  boolean isVendor(Authentication authentication);

  boolean isValidVendor(Authentication authentication, String vid);
}
