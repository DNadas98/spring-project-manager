package com.codecool.tasx.service.company;

import com.codecool.tasx.controller.dto.company.CompanyCreateRequestDto;
import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.company.CompanyResponsePublicDTO;
import com.codecool.tasx.controller.dto.company.CompanyUpdateRequestDto;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.CompanyDao;
import com.codecool.tasx.model.requests.RequestStatus;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.service.auth.UserProvider;
import com.codecool.tasx.service.converter.CompanyConverter;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
  private final CompanyDao companyDao;
  private final CompanyConverter companyConverter;
  private final UserProvider userProvider;
  private final Logger logger;

  @Autowired
  public CompanyService(
    CompanyDao companyDao, CompanyConverter companyConverter, UserProvider userProvider) {
    this.companyDao = companyDao;
    this.companyConverter = companyConverter;
    this.userProvider = userProvider;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @Transactional
  public List<CompanyResponsePublicDTO> getCompaniesWithoutUser() throws UnauthorizedException {
    User user = userProvider.getAuthenticatedUser();
    List<Company> companies = companyDao.findAllWithoutEmployeeAndJoinRequest(
      user, List.of(RequestStatus.PENDING, RequestStatus.DECLINED));
    return companyConverter.getCompanyResponsePublicDtos(companies);
  }

  @Transactional
  public List<CompanyResponsePublicDTO> getCompaniesWithUser() throws UnauthorizedException {
    User user = userProvider.getAuthenticatedUser();
    List<Company> companies = user.getCompanies();
    return companyConverter.getCompanyResponsePublicDtos(companies);
  }

  @Transactional
  @PreAuthorize("hasPermission(#companyId, 'Company', Role.COMPANY_EMPLOYEE)")
  public CompanyResponsePrivateDTO getCompanyById(Long companyId)
    throws CompanyNotFoundException, UnauthorizedException {
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    return companyConverter.getCompanyResponsePrivateDto(company);
  }

  @Transactional(rollbackOn = Exception.class)
  public CompanyResponsePrivateDTO createCompany(
    CompanyCreateRequestDto createRequestDto) throws ConstraintViolationException {
    User user = userProvider.getAuthenticatedUser();
    Company company = companyDao.save(
      new Company(createRequestDto.name(), createRequestDto.description(), user));
    company.addEmployee(user);
    return companyConverter.getCompanyResponsePrivateDto(company);
  }

  @Transactional(rollbackOn = Exception.class)
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

  @Transactional(rollbackOn = Exception.class)
  @PreAuthorize("hasPermission(#companyId, 'Company', 'COMPANY_ADMIN')")
  public void deleteCompany(Long companyId) {
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    companyDao.delete(company);
  }
}
