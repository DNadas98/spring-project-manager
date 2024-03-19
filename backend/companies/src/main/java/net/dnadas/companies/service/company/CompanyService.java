package net.dnadas.companies.service.company;

import lombok.RequiredArgsConstructor;
import net.dnadas.auth.exception.authentication.UnauthorizedException;
import net.dnadas.companies.dto.company.CompanyCreateRequestDto;
import net.dnadas.companies.dto.company.CompanyResponsePrivateDTO;
import net.dnadas.companies.dto.company.CompanyResponsePublicDTO;
import net.dnadas.companies.dto.company.CompanyUpdateRequestDto;
import net.dnadas.companies.exception.company.CompanyNotFoundException;
import net.dnadas.companies.model.company.Company;
import net.dnadas.companies.model.company.CompanyDao;
import net.dnadas.companies.model.request.RequestStatus;
import net.dnadas.companies.service.converter.CompanyConverter;
import net.dnadas.companies.service.user.UserProvider;
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
    Long userId = userProvider.getAuthenticatedUserId();
    List<Company> companies = companyDao.findAllWithoutEmployeeAndJoinRequest(
      userId, List.of(RequestStatus.PENDING, RequestStatus.DECLINED));
    return companyConverter.getCompanyResponsePublicDtos(companies);
  }

  @Transactional(readOnly = true)
  public List<CompanyResponsePublicDTO> getCompaniesWithUser() throws UnauthorizedException {
    Long userId = userProvider.getAuthenticatedUserId();
    List<Company> companies = companyDao.findAllWithEmployee(userId).stream().toList();
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
    Long userId = userProvider.getAuthenticatedUserId();
    Company company = new Company(
      createRequestDto.name(), createRequestDto.description(), userId);
    company.addEmployee(userId);
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
