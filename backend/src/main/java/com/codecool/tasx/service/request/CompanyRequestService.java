package com.codecool.tasx.service.request;

import com.codecool.tasx.controller.dto.requests.CompanyJoinRequestResponseDto;
import com.codecool.tasx.controller.dto.requests.CompanyJoinRequestUpdateDto;
import com.codecool.tasx.exception.company.CompanyJoinRequestNotFoundException;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.exception.company.DuplicateCompanyJoinRequestException;
import com.codecool.tasx.exception.company.UserAlreadyInCompanyException;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.CompanyDao;
import com.codecool.tasx.model.requests.CompanyJoinRequest;
import com.codecool.tasx.model.requests.CompanyJoinRequestDao;
import com.codecool.tasx.model.requests.RequestStatus;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.service.auth.UserProvider;
import com.codecool.tasx.service.converter.CompanyConverter;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyRequestService {
  private final CompanyDao companyDao;
  private final CompanyJoinRequestDao requestDao;
  private final UserProvider userProvider;
  private final CompanyConverter companyConverter;
  private final Logger logger;

  @Autowired
  public CompanyRequestService(
    CompanyDao companyDao, CompanyJoinRequestDao requestDao, UserProvider userProvider,
    CompanyConverter companyConverter) {
    this.companyDao = companyDao;
    this.requestDao = requestDao;
    this.userProvider = userProvider;
    this.companyConverter = companyConverter;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @Transactional
  public CompanyJoinRequestResponseDto createJoinRequest(Long companyId) {
    User user = userProvider.getAuthenticatedUser();
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    if (company.getEmployees().contains(user)) {
      throw new UserAlreadyInCompanyException();
    }
    Optional<CompanyJoinRequest> duplicateRequest = requestDao.findOneByCompanyAndUser(
      company, user);
    if (duplicateRequest.isPresent()) {
      throw new DuplicateCompanyJoinRequestException();
    }
    CompanyJoinRequest savedRequest = requestDao.save(new CompanyJoinRequest(company, user));
    return companyConverter.getCompanyJoinRequestResponseDto(savedRequest);
  }

  @Transactional
  @PreAuthorize("hasPermission(#companyId, 'Company', Role.COMPANY_ADMIN)")
  public List<CompanyJoinRequestResponseDto> getJoinRequestsOfCompany(Long companyId) {
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    List<CompanyJoinRequest> requests = requestDao.findByCompanyAndStatus(
      company,
      RequestStatus.PENDING);
    return companyConverter.getCompanyJoinRequestResponseDtos(requests);
  }

  @Transactional
  public List<CompanyJoinRequestResponseDto> getJoinRequestsOfUser() {
    User user = userProvider.getAuthenticatedUser();
    List<CompanyJoinRequest> requests = requestDao.findByUser(user);
    return companyConverter.getCompanyJoinRequestResponseDtos(requests);
  }

  @PreAuthorize("hasPermission(#companyId, 'Company', Role.COMPANY_ADMIN)")
  @Transactional(rollbackOn = Exception.class)
  public void handleJoinRequest(
    Long companyId, Long requestId, CompanyJoinRequestUpdateDto updateDto) {
    CompanyJoinRequest request = requestDao.findByIdAndCompanyId(requestId, companyId).orElseThrow(
      () -> new CompanyJoinRequestNotFoundException(requestId));
    request.setStatus(updateDto.status());
    if (request.getStatus().equals(RequestStatus.APPROVED)) {
      request.getCompany().addEmployee(request.getUser());
      requestDao.delete(request);
    }
  }
}
