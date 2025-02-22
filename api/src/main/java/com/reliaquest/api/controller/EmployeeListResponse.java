package com.reliaquest.api.controller;

import com.reliaquest.api.model.Employee;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeListResponse {
    private List<Employee> data;
}
