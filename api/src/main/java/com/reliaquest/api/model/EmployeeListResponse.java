package com.reliaquest.api.model;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeListResponse {
    private List<Employee> data;
}
