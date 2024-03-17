package net.dnadas.monolith.service.company;

import lombok.RequiredArgsConstructor;
import net.dnadas.monolith.dto.company.CompanyCreateRequestDto;
import net.dnadas.monolith.dto.company.CompanyResponsePrivateDTO;
import net.dnadas.monolith.dto.company.CompanyResponsePublicDTO;
import net.dnadas.monolith.dto.company.CompanyUpdateRequestDto;
import net.dnadas.monolith.auth.exception.authentication.UnauthorizedException;
import net.dnadas.monolith.exception.company.CompanyNotFoundException;
import net.dnadas.monolith.model.company.Company;
import net.dnadas.monolith.model.company.CompanyDao;
import net.dnadas.monolith.model.request.RequestStatus;
import net.dnadas.monolith.auth.model.user.ApplicationUser;
import net.dnadas.monolith.auth.service.user.UserProvider;
import net.dnadas.monolith.service.converter.CompanyConverter;
import org.hibernate.Hibernate;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
  private final CompanyDao companyDao;
  private final CompanyConverter companyConverter;
  private final UserProvider userProvider;

  public List<CompanyResponsePublicDTO> getAllCompanies() throws UnauthorizedException {
    List<Company> companies = companyDao.findAll();
    return companyConverter.getCompanyResponsePublicDtos(companies);
  }

  @Transactional(readOnly = true)
  public List<CompanyResponsePublicDTO> getCompaniesWithoutUser() throws UnauthorizedException {
    ApplicationUser applicationUser = userProvider.getAuthenticatedUser();
    List<Company> companies = companyDao.findAllWithoutEmployeeAndJoinRequest(
      applicationUser, List.of(RequestStatus.PENDING, RequestStatus.DECLINED));
    return companyConverter.getCompanyResponsePublicDtos(companies);
  }

  @Transactional(readOnly = true)
  public List<CompanyResponsePublicDTO> getCompaniesWithUser() throws UnauthorizedException {
    ApplicationUser applicationUser = userProvider.getAuthenticatedUser();
    Hibernate.initialize(applicationUser.getEmployeeCompanies());
    List<Company> companies = applicationUser.getEmployeeCompanies().stream().toList();
    return companyConverter.getCompanyResponsePublicDtos(companies);
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(#companyId, 'Company', 'COMPANY_EMPLOYEE')")
  public CompanyResponsePrivateDTO getCompanyById(Long companyId)
    throws CompanyNotFoundException, UnauthorizedException {
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    return companyConverter.getCompanyResponsePrivateDto(company);
  }

  @Transactional(rollbackFor = Exception.class)
  public CompanyResponsePrivateDTO createCompany(
    CompanyCreateRequestDto createRequestDto) throws ConstraintViolationException {
    ApplicationUser applicationUser = userProvider.getAuthenticatedUser();
    Company company = new Company(
      createRequestDto.name(), createRequestDto.description(), applicationUser);
    company.addEmployee(applicationUser);
    companyDao.save(company);
    return companyConverter.getCompanyResponsePrivateDto(company);
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#companyId, 'Company', 'COMPANY_EDITOR')")
  public CompanyResponsePrivateDTO updateCompany(
    CompanyUpdateRequestDto updateRequestDto, Long companyId) throws ConstraintViolationException {
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    company.setName(updateRequestDto.name());
    company.setDescription(updateRequestDto.description());
    Company updatedCompany = companyDao.save(company);
    return companyConverter.getCompanyResponsePrivateDto(updatedCompany);
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#companyId, 'Company', 'COMPANY_ADMIN')")
  public void deleteCompany(Long companyId) {
    Company company = companyDao.findById(companyId).orElseThrow(() ->
      new CompanyNotFoundException(companyId));
    companyDao.delete(company);
  }
}
