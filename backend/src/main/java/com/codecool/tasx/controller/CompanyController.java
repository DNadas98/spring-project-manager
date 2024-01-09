package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.company.CompanyCreateRequestDto;
import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.company.CompanyResponsePublicDTO;
import com.codecool.tasx.controller.dto.company.CompanyUpdateRequestDto;
import com.codecool.tasx.service.company.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {
  private final CompanyService companyService;
  private final Logger logger;

  @Autowired
  public CompanyController(CompanyService companyService) {
    this.companyService = companyService;
    logger = LoggerFactory.getLogger(this.getClass());
  }


  @GetMapping()
  public ResponseEntity<?> getAllCompanies(
    @RequestParam(name = "withUser") Boolean withUser) {
    List<CompanyResponsePublicDTO> companies;
    if (withUser) {
      companies = companyService.getCompaniesWithUser();
    } else {
      companies = companyService.getCompaniesWithoutUser();
    }
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", companies));
  }

  @GetMapping("/{companyId}")
  public ResponseEntity<?> getCompanyById(
    @PathVariable Long companyId) {
    CompanyResponsePrivateDTO company = companyService.getCompanyById(companyId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", company));
  }

  @PostMapping
  public ResponseEntity<?> createCompany(
    @RequestBody CompanyCreateRequestDto createRequestDto) {
    CompanyResponsePrivateDTO companyResponseDetails = companyService.createCompany(
      createRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(
      Map.of("message", "Company created successfully", "data", companyResponseDetails));
  }

  @PutMapping("/{companyId}")
  public ResponseEntity<?> updateCompany(
    @PathVariable Long companyId, @RequestBody CompanyUpdateRequestDto updateRequestDto) {
    CompanyResponsePrivateDTO companyResponseDetails = companyService.updateCompany(
      updateRequestDto, companyId);

    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Company with ID " + companyId + " updated successfully", "data",
        companyResponseDetails));
  }

  @DeleteMapping("/{companyId}")
  public ResponseEntity<?> deleteCompany(@PathVariable Long companyId) {
    companyService.deleteCompany(companyId);

    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Company with ID " + companyId + " deleted successfully"));
  }
}