package com.esprofiler.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.esprofiler.repository.VendorRepository;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

  @Mock private VendorRepository vendorRepository;

  private Authentication authentication;

  @InjectMocks private AuthenticationServiceImpl authenticationServiceImpl;

  private OAuth2AuthenticationToken oauth2AuthenticationToken;

  @BeforeEach
  void setUp() {
    List<GrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("given_name", "John");
    attributes.put("family_name", "Doe");
    attributes.put("name", "John Doe");
    attributes.put("vendor_id", "2cc40d4d-36c7-4c60-b15c-761dded8abb5");

    OAuth2User principal = new DefaultOAuth2User(authorities, attributes, "name");

    oauth2AuthenticationToken = mock(OAuth2AuthenticationToken.class);
    when(oauth2AuthenticationToken.getPrincipal()).thenReturn(principal);
  }

  @Test
  void getUserDetails_WithOAuth2Authentication_ReturnsCorrectDetails() {
    List<GrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    when(oauth2AuthenticationToken.getAuthorities()).thenReturn(authorities);
    Map<String, Object> userDetails =
        authenticationServiceImpl.getUserDetails(oauth2AuthenticationToken);

    assertEquals("John Doe", userDetails.get("name"));
    assertEquals("[ROLE_USER]", userDetails.get("grantedAuthorities").toString());
  }

  @Test
  void verifyVendorVID_WithMatchingVID_ReturnsTrue() {
    boolean result = authenticationServiceImpl.verifyVendorVID(oauth2AuthenticationToken, "2cc40d4d-36c7-4c60-b15c-761dded8abb5");
    assertTrue(result);
  }
}
