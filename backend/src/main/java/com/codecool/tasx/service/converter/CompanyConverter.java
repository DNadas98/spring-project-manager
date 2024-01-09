package com.codecool.tasx.service.converter;

import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.company.CompanyResponsePublicDTO;
import com.codecool.tasx.controller.dto.requests.CompanyJoinRequestResponseDto;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.requests.CompanyJoinRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CompanyConverter {
  private final UserConverter userConverter;
  private final ProjectConverter projectConverter;
  private final RewardConverter rewardConverter;

  @Autowired
  public CompanyConverter(
    UserConverter userConverter, ProjectConverter projectConverter,
    RewardConverter rewardConverter) {
    this.userConverter = userConverter;
    this.projectConverter = projectConverter;
    this.rewardConverter = rewardConverter;
  }

  public List<CompanyResponsePublicDTO> getCompanyResponsePublicDtos(List<Company> companies) {
    return companies.stream().map(
      company -> getCompanyResponsePublicDto(company)).collect(Collectors.toList());
  }

  public CompanyResponsePrivateDTO getCompanyResponsePrivateDto(Company company) {
    return new CompanyResponsePrivateDTO(company.getId(), company.getName(),
      company.getDescription(), userConverter.getUserResponsePublicDto(company.getCompanyOwner()),
      userConverter.getUserResponsePublicDtos(company.getEmployees()),
      rewardConverter.getRewardResponseDtos(company.getRewards()));
  }

  public CompanyResponsePublicDTO getCompanyResponsePublicDto(Company company) {
    return new CompanyResponsePublicDTO(company.getId(), company.getName(),
      company.getDescription());
  }

  public CompanyJoinRequestResponseDto getCompanyJoinRequestResponseDto(
    CompanyJoinRequest request) {
    return new CompanyJoinRequestResponseDto(request.getId(),
      getCompanyResponsePublicDto(request.getCompany()),
      userConverter.getUserResponsePublicDto(request.getUser()), request.getStatus());
  }

  public List<CompanyJoinRequestResponseDto> getCompanyJoinRequestResponseDtos(
    List<CompanyJoinRequest> requests) {
    return requests.stream().map(request -> getCompanyJoinRequestResponseDto(request)).collect(
      Collectors.toList());
  }
}
