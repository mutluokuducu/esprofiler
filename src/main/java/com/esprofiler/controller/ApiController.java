package com.esprofiler.controller;

import static com.esprofiler.constant.Constants.AUTHENTICATED;
import static com.esprofiler.constant.Constants.BEEN_AUTHENTICATED;
import static com.esprofiler.constant.Constants.ERROR;
import static com.esprofiler.constant.Constants.INVALID_ACCESS_OR_VID;
import static com.esprofiler.constant.Constants.MESSAGE;
import static com.esprofiler.constant.Constants.SPECIFIC_VENDOR;
import static com.esprofiler.constant.Constants.VENDORS;
import static com.esprofiler.constant.Constants.VENDORS_VID;
import static com.esprofiler.constant.Constants.VENDOR_GROUP;
import static com.esprofiler.constant.Constants.VID;

import com.esprofiler.config.IsVendor;
import com.esprofiler.service.AuthenticationServiceImpl;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

  private final AuthenticationServiceImpl authenticationServiceImpl;

  public ApiController(AuthenticationServiceImpl authenticationServiceImpl) {
    this.authenticationServiceImpl = authenticationServiceImpl;
  }

  @GetMapping(AUTHENTICATED)
  public Map<String, Object> authenticated(Authentication authentication) {
    Map<String, Object> userDetails = authenticationServiceImpl.getUserDetails(authentication);
    userDetails.put(MESSAGE, BEEN_AUTHENTICATED);
    return userDetails;
  }

  @IsVendor
  @GetMapping(VENDORS)
  public Map<String, Object> vendorDetails(Authentication authentication) {
    Map<String, Object> userDetails =
        new HashMap<>(
            authenticationServiceImpl.getUserDetails(authentication)); // Create a mutable copy
    userDetails.put(MESSAGE, VENDOR_GROUP);
    return userDetails;
  }

  @GetMapping(VENDORS_VID)
  public ResponseEntity<Map<String, Object>> specificVendor(
      Authentication authentication, @PathVariable("vid") String vid) {
    boolean hasCorrectVID = authenticationServiceImpl.verifyVendorVID(authentication, vid);

    if (!hasCorrectVID) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(ERROR, INVALID_ACCESS_OR_VID));
    }

    Map<String, Object> userDetails =
        new HashMap<>(
            authenticationServiceImpl.getUserDetails(authentication)); // Make a mutable copy
    userDetails.put(MESSAGE, SPECIFIC_VENDOR);
    userDetails.put(VID, vid);

    return ResponseEntity.ok(userDetails);
  }
}
