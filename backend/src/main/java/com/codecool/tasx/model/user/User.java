package com.codecool.tasx.model.user;

import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.project.Project;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "user_account")
public class User implements UserDetails, OAuth2User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = true)
  private String username;

  @Column(unique = true, nullable = false)
  private String email;

  private String password;

  @Enumerated(EnumType.STRING)
  private Set<Role> roles;

  @OneToMany(mappedBy = "companyOwner", fetch = FetchType.EAGER)
  private List<Company> ownedCompanies;

  @ManyToMany(mappedBy = "employees", fetch = FetchType.EAGER)
  private List<Company> companies;

  @OneToMany(mappedBy = "projectOwner", fetch = FetchType.EAGER)
  private List<Project> ownedProjects;

  @ManyToMany(mappedBy = "assignedEmployees", fetch = FetchType.EAGER)
  private List<Project> projects;

  private boolean OAuth2User;

  private long score;

  public User() {
  }

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.roles = new HashSet<>();
    roles.add(Role.USER);
    this.ownedCompanies = new ArrayList<>();
    this.companies = new ArrayList<>();
    this.ownedProjects = new ArrayList<>();
    this.OAuth2User = false;
    score = 0;
  }

  public User(String username, String email) {
    this.username = username;
    this.email = email;
    this.roles = new HashSet<>();
    roles.add(Role.USER);
    this.ownedCompanies = new ArrayList<>();
    this.companies = new ArrayList<>();
    this.ownedProjects = new ArrayList<>();
    this.OAuth2User = true;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Returns the actual username, since getUsername is reserved for the subject, which is the email
   */
  public String getActualUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public long getScore() {
    return score;
  }

  public void setScore(long score) {
    this.score = score;
  }

  public Set<Role> getRoles() {
    return Set.copyOf(roles);
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public List<Company> getOwnedCompanies() {
    return List.copyOf(ownedCompanies);
  }

  public void setOwnedCompanies(List<Company> ownedCompanies) {
    this.ownedCompanies = ownedCompanies;
  }

  public List<Project> getProjects() {
    return projects;
  }

  public void setProjects(List<Project> projects) {
    this.projects = projects;
  }

  public List<Company> getCompanies() {
    return List.copyOf(companies);
  }

  public void setCompanies(List<Company> companies) {
    this.companies = companies;
  }

  public List<Project> getOwnedProjects() {
    return List.copyOf(ownedProjects);
  }

  public void setOwnedProjects(List<Project> ownedProjects) {
    this.ownedProjects = ownedProjects;
  }

  public boolean isOAuth2User() {
    return this.OAuth2User;
  }

  public void setOAuth2User(boolean value) {
    this.OAuth2User = value;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(
      Collectors.toSet());
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * In {@link UserDetails} the subject is always named "username"<br>
   * In our application the subject is the email, but since {@link User} implements
   * {@link UserDetails}, there must be a method named "getUsername" which actually returns the
   * email...
   */
  @Override
  public String getUsername() {
    return email;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public boolean isAccountNonExpired() {
    //TODO: impl
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    //TODO: impl
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    //TODO: impl
    return true;
  }

  @Override
  public boolean isEnabled() {
    //TODO: impl
    return true;
  }

  @Override
  public <A> A getAttribute(String name) {
    //TODO: impl
    return (A) this.email;
  }

  @Override
  public Map<String, Object> getAttributes() {
    //TODO: impl
    return null;
  }

  @Override
  public String getName() {
    //TODO: impl
    return this.email;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, email, roles);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof User user)) {
      return false;
    }
    return Objects.equals(id, user.id) && Objects.equals(
      username, user.username) && Objects.equals(email, user.email) && Objects.equals(
      roles, user.roles);
  }

  @Override
  public String toString() {
    return "User{" +
      "id=" + id +
      ", username='" + username + '\'' +
      ", email='" + email + '\'' +
      ", roles=" + roles +
      '}';
  }
}

