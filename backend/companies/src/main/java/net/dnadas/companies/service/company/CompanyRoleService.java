package net.dnadas.companies.service.company;

import lombok.RequiredArgsConstructor;
import net.dnadas.auth.dto.authentication.UserInfoDto;
import net.dnadas.auth.dto.user.UserResponsePublicDto;
import net.dnadas.companies.exception.company.CompanyNotFoundException;
import net.dnadas.companies.model.authorization.PermissionType;
import net.dnadas.companies.model.company.Company;
import net.dnadas.companies.model.company.CompanyDao;
import net.dnadas.companies.service.authorization.CustomPermissionEvaluator;
import net.dnadas.companies.service.user.UserProvider;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompanyRoleService {
  private final CompanyDao companyDao;
  private final UserProvider userProvider;
  private final CustomPermissionEvaluator permissionEvaluator;

  @Transactional(readOnly = true)
  public Set<PermissionType> getUserPermissionsForCompany(Long companyId) {
    UserInfoDto userInfo = userProvider.getUserInfo();
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));

    Set<PermissionType> permissions = new HashSet<>();
    permissions.add(PermissionType.COMPANY_EMPLOYEE);
    if (permissionEvaluator.hasCompanyEditorAccess(userInfo, company)) {
      permissions.add(PermissionType.COMPANY_EDITOR);
    }
    if (permissionEvaluator.hasCompanyAdminAccess(userInfo, company)) {
      permissions.add(PermissionType.COMPANY_ADMIN);
    }
    return permissions;
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(#companyId, 'Company', 'COMPANY_ADMIN')")
  public List<UserResponsePublicDto> getEmployees(Long companyId) {
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    //TODO: implement
    return company.getEmployees().stream().map(
      employeeId -> new UserResponsePublicDto(employeeId, "username")).toList();
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#companyId, 'Company', 'COMPANY_ADMIN')")
  public void addEmployee(Long companyId, Long userId) {
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    company.addEmployee(userId);
    companyDao.save(company);
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#companyId, 'Company', 'COMPANY_ADMIN')")
  public void removeEmployee(Long companyId, Long userId) {
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    company.removeEmployee(userId);
    companyDao.save(company);
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(#companyId, 'Company', 'COMPANY_ADMIN')")
  public List<UserResponsePublicDto> getEditors(Long companyId) {
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    //TODO: implement
    return List.of();
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#companyId, 'Company', 'COMPANY_ADMIN')")
  public void addEditor(Long companyId, Long userId) {
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    company.addEditor(userId);
    companyDao.save(company);
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#companyId, 'Company', 'COMPANY_ADMIN')")
  public void removeEditor(Long companyId, Long userId) {
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    company.removeEditor(userId);
    companyDao.save(company);
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(#companyId, 'Company', 'COMPANY_ADMIN')")
  public List<UserResponsePublicDto> getAdmins(Long companyId) {
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    //TODO: implement
    return List.of();
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#companyId, 'Company', 'COMPANY_ADMIN')")
  public void addAdmin(Long companyId, Long userId) {
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    company.addAdmin(userId);
    companyDao.save(company);
  }

  @Transactional(rollbackFor = Exception.class)
  @PreAuthorize("hasPermission(#companyId, 'Company', 'COMPANY_ADMIN')")
  public void removeAdmin(Long companyId, Long userId) {
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    company.removeAdmin(userId);
    companyDao.save(company);
  }
}
