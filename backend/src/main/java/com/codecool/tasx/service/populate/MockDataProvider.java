package com.codecool.tasx.service.populate;

import org.springframework.stereotype.Service;

@Service
public class MockDataProvider {/*
  private final AuthenticationService authService;
  private final CompanyDao companyDao;
  private final ProjectDao projectDao;
  private final TaskDao taskDao;
  private final ExpenseDao expenseDao;
  private final RewardDao rewardDao;
  private final CompanyJoinRequestDao joinRequestDao;

  @Autowired
  public MockDataProvider(
    AuthenticationService authService, CompanyDao companyDao,
    ProjectDao projectDao, TaskDao taskDao,
    ExpenseDao expenseDao, RewardDao rewardDao, CompanyJoinRequestDao joinRequestDao) {
    this.authService = authService;
    this.companyDao = companyDao;
    this.projectDao = projectDao;
    this.taskDao = taskDao;
    this.expenseDao = expenseDao;
    this.rewardDao = rewardDao;
    this.joinRequestDao = joinRequestDao;
  }

  @PostConstruct
  public void populate() {
    User user1 = new User("Dani", "dani@tasx.com", "asdasdasd");
    authService.register(new RegisterRequestDto(
      user1.getActualUsername(), user1.getEmail(), user1.getPassword()));
    User user2 = new User("Ori", "ori@tasx.com", "123123123");
    authService.register(new RegisterRequestDto(
      user2.getActualUsername(), user2.getEmail(), user2.getPassword()));

    Company company1 = new Company("Company1", "Company 1", user1);
    companyDao.save(company1);
    Company company2 = new Company("Company2", "Company 2", user2);
    companyDao.save(company2);

    Project project1 = new Project("Project1", "project 1", LocalDateTime.now(),
      LocalDateTime.now().plusHours(1), user1, company1);
    projectDao.save(project1);
    Project project2 = new Project("Project2", "project 2", LocalDateTime.now(),
      LocalDateTime.now().plusHours(1), user2, company2);
    projectDao.save(project2);

    Task task1 = new Task("Task 1", "asdasd", Importance.NICE_TO_HAVE, 1, LocalDateTime.now(),
      LocalDateTime.now().plusHours(1), TaskStatus.BACKLOG, user1, project1);
    taskDao.save(task1);
    Task task2 = new Task("Task 2", "asdasd", Importance.MUST_HAVE, 1, LocalDateTime.now(),
      LocalDateTime.now().plusHours(1), TaskStatus.IN_PROGRESS, user1, project1);
    taskDao.save(task2);
    Task task3 = new Task("Task 3", "asdasd", Importance.NICE_TO_HAVE, 5, LocalDateTime.now(),
      LocalDateTime.now().plusHours(1), TaskStatus.BACKLOG, user2, project2);
    taskDao.save(task3);

    CompanyJoinRequest joinRequest1 = new CompanyJoinRequest(company1, user2);
    joinRequestDao.save(joinRequest1);
    CompanyJoinRequest joinRequest2 = new CompanyJoinRequest(company2, user1);
    joinRequestDao.save(joinRequest2);
  }*/
}
