package com.esprofiler.controller;

import static com.esprofiler.constant.Constants.ERROR;
import static com.esprofiler.constant.Constants.MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.esprofiler.service.AuthenticationServiceImpl;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class ApiControllerTest {

  @Mock private AuthenticationServiceImpl authenticationServiceImpl;

  @Mock private Authentication authentication;

  @InjectMocks private ApiController apiController;

  @Test
  void authenticated_ReturnsUserDetails() {
    Map<String, Object> serviceReturnedDetails = new HashMap<>();
    serviceReturnedDetails.put("name", "John Doe");
    serviceReturnedDetails.put("role", "USER");

    when(authenticationServiceImpl.getUserDetails(any(Authentication.class)))
        .thenReturn(serviceReturnedDetails);
    Map<String, Object> result = apiController.authenticated(authentication);

    String expectedMessage = "Looks like you have been authenticated";
    result.put(MESSAGE, expectedMessage);
    assertEquals(expectedMessage, result.get(MESSAGE));
    assertEquals("John Doe", result.get("name"));
    assertEquals("USER", result.get("role"));
  }

  @Test
  void vendorDetails_ReturnsVendorDetails() {
    Map<String, Object> serviceReturnedDetails = new HashMap<>();
    serviceReturnedDetails.put("name", "Vendor Name");
    serviceReturnedDetails.put("role", "VENDOR");

    when(authenticationServiceImpl.getUserDetails(any(Authentication.class)))
        .thenReturn(serviceReturnedDetails);

    Map<String, Object> result = apiController.vendorDetails(authentication);

    assertEquals("Welcome to the vendor group!", result.get(MESSAGE));
    assertEquals("Vendor Name", result.get("name"));
    assertEquals("VENDOR", result.get("role"));
  }

  @Test
  void specificVendor_WithCorrectVID_ReturnsVendorDetails() {
    String vid = "2cc40d4d-36c7-4c60-b15c-761dded8abb5";
    Map<String, Object> serviceReturnedDetails = new HashMap<>();
    serviceReturnedDetails.put("name", "John Doe");
    serviceReturnedDetails.put("role", "USER");

    when(authenticationServiceImpl.verifyVendorVID(authentication, vid)).thenReturn(true);
    when(authenticationServiceImpl.getUserDetails(any(Authentication.class)))
        .thenReturn(serviceReturnedDetails);

    ResponseEntity<Map<String, Object>> response =
        apiController.specificVendor(authentication, vid);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(authenticationServiceImpl).verifyVendorVID(authentication, vid);
    verify(authenticationServiceImpl).getUserDetails(authentication);
  }

  @Test
  void specificVendor_WithIncorrectVID_ReturnsForbidden() {
    String vid = "invalid-vid";
    when(authenticationServiceImpl.verifyVendorVID(authentication, vid)).thenReturn(false);

    ResponseEntity<Map<String, Object>> response =
        apiController.specificVendor(authentication, vid);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertEquals(Map.of(ERROR, "Invalid access or VID"), response.getBody());
    verify(authenticationServiceImpl).verifyVendorVID(authentication, vid);
    verify(authenticationServiceImpl, never()).getUserDetails(authentication);
  }
}
