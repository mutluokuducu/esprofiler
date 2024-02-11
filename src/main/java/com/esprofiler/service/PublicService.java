package com.esprofiler.service;

import static com.esprofiler.constant.Constants.ALL_TO_SEE;
import static com.esprofiler.constant.Constants.ANONYMOUS;
import static com.esprofiler.constant.Constants.MESSAGE;
import static com.esprofiler.constant.Constants.NAME;

import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class PublicService {

  /**
   * Provides a public message and an anonymous name.
   *
   * @return A map containing the message and name.
   */
  public Map<String, String> getPublicMessage() {
    return Map.of(
        MESSAGE, ALL_TO_SEE,
        NAME, ANONYMOUS);
  }
}
