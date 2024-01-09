package com.codecool.tasx.controller.dto.company.project.task.expense;

public record ExpenseCreateRequestDto(Long taskId, String name, Double price, Boolean paid) {
}
