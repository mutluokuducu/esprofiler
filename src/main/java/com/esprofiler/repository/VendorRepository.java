package com.esprofiler.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

  /**
   * Finds a vendor by their associated username and vendor ID.
   *
   * @param username The username associated with the vendor.
   * @param vid The vendor ID.
   * @return An Optional containing the found vendor if available, otherwise an empty Optional.
   */
  Optional<Vendor> findByUsernameAndVid(String username, String vid);
}
