package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.requests.CompanyJoinRequestResponseDto;
import com.codecool.tasx.controller.dto.requests.CompanyJoinRequestUpdateDto;
import com.codecool.tasx.service.request.CompanyRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/requests")
public class CompanyRequestController {
  private final CompanyRequestService requestService;
  private final Logger logger;

  @Autowired
  public CompanyRequestController(CompanyRequestService requestService) {
    this.requestService = requestService;
    logger = LoggerFactory.getLogger(this.getClass());
  }

  @GetMapping()
  public ResponseEntity<?> readJoinRequestsOfCompany(
    @PathVariable Long companyId) {

    List<CompanyJoinRequestResponseDto> requests = requestService.getJoinRequestsOfCompany(
      companyId);

    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", requests));
  }

  @PostMapping()
  public ResponseEntity<?> joinCompany(@PathVariable Long companyId) {
    CompanyJoinRequestResponseDto createdRequest = requestService.createJoinRequest(companyId);

    return ResponseEntity.status(HttpStatus.CREATED).body(
      Map.of("message", "Request created successfully", "data", createdRequest));
  }

  @PutMapping("/{requestId}")
  public ResponseEntity<?> updateJoinRequestById(
    @PathVariable Long companyId,
    @PathVariable Long requestId, @RequestBody CompanyJoinRequestUpdateDto requestDto) {

    requestService.handleJoinRequest(companyId, requestId, requestDto);

    //TODO: notify the user who requested to join...
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Request updated successfully"));
  }
}