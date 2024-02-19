package com.codecool.tasx.controller;

import com.codecool.tasx.dto.company.CompanyCreateRequestDto;
import com.codecool.tasx.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.dto.company.CompanyResponsePublicDTO;
import com.codecool.tasx.dto.company.CompanyUpdateRequestDto;
import com.codecool.tasx.service.company.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {
  private final CompanyService companyService;

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
  public ResponseEntity<?> getCompanyById(@PathVariable Long companyId) {
    CompanyResponsePrivateDTO company = companyService.getCompanyById(companyId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", company));
  }

  @PostMapping
  public ResponseEntity<?> createCompany(@RequestBody CompanyCreateRequestDto createRequestDto) {
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