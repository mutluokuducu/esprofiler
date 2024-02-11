package com.esprofiler.service;

import com.esprofiler.repository.VendorRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  private final VendorRepository vendorRepository; // Assuming a repository interface

  public AuthenticationServiceImpl(VendorRepository vendorRepository) {
    this.vendorRepository = vendorRepository;
  }
  /**
   * Extracts user details from the Authentication object, providing a consistent base for responses.
   *
   * @param authentication The Authentication object containing the user's details and authorities.
   * @return A map of user details, including the full name and granted authorities.
   */
  public Map<String, Object> getUserDetails(Authentication authentication) {
    Map<String, Object> userDetails = new HashMap<>();

    if (authentication instanceof OAuth2AuthenticationToken authToken) {
      Map<String, Object> attributes = authToken.getPrincipal().getAttributes();

      String fullName = attributes.getOrDefault("given_name", "") + " " + attributes.getOrDefault("family_name", "");
      userDetails.put("name", fullName.trim());
    } else {
      userDetails.put("name", authentication.getName());
    }

    List<String> authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();
    userDetails.put("grantedAuthorities", authorities);

    return userDetails;
  }

  /**
   * Verifies if the authenticated user's VID matches the expected VID.
   *
   * @param authentication The Authentication object containing the user's details.
   * @param expectedVID The VID to check against the authenticated user's VID.
   * @return true if the authenticated user's VID matches the expected VID, false otherwise.
   */
  public boolean verifyVendorVID(Authentication authentication, String expectedVID) {
    if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
      Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
      String userVID = (String) attributes.get("vendor_id");
      return expectedVID.equals(userVID);
    }
    return false;
  }

  /**
   * If used controller  @PreAuthorize("hasAuthority('CGROUP_VENDOR')") no need
   * * Checks if the authenticated user has the "CGROUP_VENDOR" authority.
   *
   * @param authentication The Authentication object containing the user's details and authorities.
   * @return true if the user has the "CGROUP_VENDOR" authority, false otherwise.
   */
  public boolean isVendor(Authentication authentication) {
    return authentication.getAuthorities().stream()
        .anyMatch(grantedAuthority -> "CGROUP_VENDOR".equals(grantedAuthority.getAuthority()));
  }

  /**
   * Validates if the authenticated user is associated with the provided vendor ID.
   *
   * @param authentication The Authentication object containing the user's details.
   * @param vid The vendor ID to be validated against the authenticated user.
   * @return true if the user is associated with the vendor ID, false otherwise.
   */
  public boolean isValidVendor(Authentication authentication, String vid) {
    String username = authentication.getName(); // Or another unique identifier

    return vendorRepository.findByUsernameAndVid(username, vid).isPresent();
  }
}
