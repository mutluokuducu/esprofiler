package com.esprofiler.controller;

import static com.esprofiler.constant.Constants.UNAUTHENTICATED;

import com.esprofiler.service.PublicService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicApiController {

  private final PublicService publicService;

  public PublicApiController(PublicService publicService) {
    this.publicService = publicService;
  }

  @GetMapping(UNAUTHENTICATED)
  public ResponseEntity<Map<String, String>> unauthenticated() {
    return ResponseEntity.ok(publicService.getPublicMessage());
  }
}
