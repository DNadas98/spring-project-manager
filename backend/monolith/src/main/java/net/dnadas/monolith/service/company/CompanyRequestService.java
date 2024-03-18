package net.dnadas.monolith.service.company;

import lombok.RequiredArgsConstructor;
import net.dnadas.monolith.dto.requests.CompanyJoinRequestResponseDto;
import net.dnadas.monolith.dto.requests.CompanyJoinRequestUpdateDto;
import net.dnadas.monolith.exception.company.CompanyJoinRequestNotFoundException;
import net.dnadas.monolith.exception.company.CompanyNotFoundException;
import net.dnadas.monolith.exception.company.DuplicateCompanyJoinRequestException;
import net.dnadas.monolith.exception.company.UserAlreadyInCompanyException;
import net.dnadas.monolith.model.company.Company;
import net.dnadas.monolith.model.company.CompanyDao;
import net.dnadas.monolith.model.request.CompanyJoinRequest;
import net.dnadas.monolith.model.request.CompanyJoinRequestDao;
import net.dnadas.monolith.model.request.RequestStatus;
import net.dnadas.monolith.service.converter.CompanyConverter;
import net.dnadas.monolith.service.user.UserProvider;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyRequestService {
  private final CompanyDao companyDao;
  private final CompanyJoinRequestDao requestDao;
  private final CompanyRoleService companyRoleService;
  private final UserProvider userProvider;
  private final CompanyConverter companyConverter;

  public List<CompanyJoinRequestResponseDto> getOwnJoinRequests() {
    Long userId = userProvider.getAuthenticatedUserId();
    List<CompanyJoinRequest> requests = requestDao.findByUserId(userId);
    return companyConverter.getCompanyJoinRequestResponseDtos(requests);
  }

  @Transactional(rollbackFor = Exception.class)
  public CompanyJoinRequestResponseDto createJoinRequest(Long companyId) {
    Long userId = userProvider.getAuthenticatedUserId();
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    if (company.getEmployees().contains(userId)) {
      throw new UserAlreadyInCompanyException();
    }
    Optional<CompanyJoinRequest> duplicateRequest = requestDao.findOneByCompanyAndUserId(
      company, userId);
    if (duplicateRequest.isPresent()) {
      throw new DuplicateCompanyJoinRequestException();
    }
    CompanyJoinRequest joinRequest = new CompanyJoinRequest(company, userId);
    CompanyJoinRequest savedRequest = requestDao.save(joinRequest);
    return companyConverter.getCompanyJoinRequestResponseDto(savedRequest);
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteOwnJoinRequestById(Long requestId) {
    Long userId = userProvider.getAuthenticatedUserId();
    CompanyJoinRequest joinRequest = requestDao.findByIdAndUserId(requestId, userId).orElseThrow(
      () -> new CompanyJoinRequestNotFoundException(requestId));
    requestDao.deleteById(joinRequest.getId());
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(#companyId, 'Company', 'COMPANY_ADMIN')")
  public List<CompanyJoinRequestResponseDto> getJoinRequestsOfCompany(Long companyId) {
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    List<CompanyJoinRequest> requests = requestDao.findByCompanyAndStatus(
      company,
      RequestStatus.PENDING);
    return companyConverter.getCompanyJoinRequestResponseDtos(requests);
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#companyId, 'Company', 'COMPANY_ADMIN')")
  public void handleJoinRequest(
    Long companyId, Long requestId, CompanyJoinRequestUpdateDto updateDto) {
    CompanyJoinRequest request = requestDao.findByIdAndCompanyId(requestId, companyId).orElseThrow(
      () -> new CompanyJoinRequestNotFoundException(requestId));
    request.setStatus(updateDto.status());
    if (request.getStatus().equals(RequestStatus.APPROVED)) {
      companyRoleService.addEmployee(companyId, request.getUserId());
      requestDao.delete(request);
    }
  }
}
