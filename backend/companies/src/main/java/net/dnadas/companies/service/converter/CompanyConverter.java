package net.dnadas.companies.service.converter;

import lombok.RequiredArgsConstructor;
import net.dnadas.auth.dto.user.UserResponsePublicDto;
import net.dnadas.companies.dto.company.CompanyResponsePrivateDTO;
import net.dnadas.companies.dto.company.CompanyResponsePublicDTO;
import net.dnadas.companies.dto.requests.CompanyJoinRequestResponseDto;
import net.dnadas.companies.model.company.Company;
import net.dnadas.companies.model.request.CompanyJoinRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CompanyConverter {

  public List<CompanyResponsePublicDTO> getCompanyResponsePublicDtos(List<Company> companies) {
    return companies.stream().map(
      company -> getCompanyResponsePublicDto(company)).collect(Collectors.toList());
  }

  public CompanyResponsePrivateDTO getCompanyResponsePrivateDto(Company company) {
    return new CompanyResponsePrivateDTO(company.getId(), company.getName(),
      company.getDescription());
  }

  public CompanyResponsePublicDTO getCompanyResponsePublicDto(Company company) {
    return new CompanyResponsePublicDTO(company.getId(), company.getName(),
      company.getDescription());
  }

  @Transactional(readOnly = true)
  public CompanyJoinRequestResponseDto getCompanyJoinRequestResponseDto(
    CompanyJoinRequest request) {
    //TODO: implement
    return new CompanyJoinRequestResponseDto(request.getId(),
      getCompanyResponsePublicDto(request.getCompany()),
      new UserResponsePublicDto(request.getUserId(), "username"), request.getStatus());
  }

  public List<CompanyJoinRequestResponseDto> getCompanyJoinRequestResponseDtos(
    List<CompanyJoinRequest> requests) {
    return requests.stream().map(request -> getCompanyJoinRequestResponseDto(request)).collect(
      Collectors.toList());
  }
}
