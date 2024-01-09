package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.requests.CompanyJoinRequestResponseDto;
import com.codecool.tasx.controller.dto.requests.ProjectJoinRequestResponseDto;
import com.codecool.tasx.controller.dto.user.UserResponsePrivateDto;
import com.codecool.tasx.controller.dto.user.UserUpdateRequestDto;
import com.codecool.tasx.service.request.CompanyRequestService;
import com.codecool.tasx.service.request.ProjectRequestService;
import com.codecool.tasx.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {
  private final CompanyRequestService requestService;
  private final ProjectRequestService projectRequestService;
  private final UserService userService;

  @Autowired
  public UserController(
    CompanyRequestService requestService, ProjectRequestService projectRequestService,
    UserService userService) {
    this.requestService = requestService;
    this.projectRequestService = projectRequestService;
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<?> getOwnUserDetails() {
    UserResponsePrivateDto userDetails = userService.getOwnUserDetails();
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", userDetails));
  }

  @PutMapping
  public ResponseEntity<?> updateOwnUserDetails(
    @RequestBody UserUpdateRequestDto updateRequestDto) {
    UserResponsePrivateDto userDetails = userService.updateOwnUserDetails(updateRequestDto);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", userDetails));
  }

  @GetMapping("/requests")
  public ResponseEntity<?> getJoinRequestsOfUser() {
    List<CompanyJoinRequestResponseDto> joinRequests = requestService.getJoinRequestsOfUser();
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", joinRequests));
  }

  @GetMapping("/project-requests")
  public ResponseEntity<?> getProjectJoinRequestOfUser() {
    List<ProjectJoinRequestResponseDto> projectJoinRequests =
      projectRequestService.getJoinRequestsOfUser();
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", projectJoinRequests));
  }


}
