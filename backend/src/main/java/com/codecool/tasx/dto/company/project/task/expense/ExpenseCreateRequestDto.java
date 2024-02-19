package com.codecool.tasx.dto.company.project.task.expense;

public record ExpenseCreateRequestDto(String name, Double price, Boolean paid) {
}
