package com.codecool.tasx.controller.dto.company.project.task.expense;

public record ExpenseResponseDto(Long expenseId, String name, Double price, Boolean paid) {
}
