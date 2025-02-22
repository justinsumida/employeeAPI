package com.reliaquest.api.service;

import com.reliaquest.api.model.EmployeeDeleteResponse;
import com.reliaquest.api.model.EmployeeListResponse;
import com.reliaquest.api.model.EmployeeResponse;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeDelete;
import com.reliaquest.api.model.EmployeeInput;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final WebClient client = WebClient.builder().baseUrl("http://localhost:8112/api/v1/employee").build();

    public List<Employee> getAllEmployees() {
        return Optional.ofNullable(client.get().retrieve().bodyToMono(EmployeeListResponse.class).block())
                .map(EmployeeListResponse::getData)
                .orElse(null);
    }

    public List<Employee> getEmployeesByName(String name) {
        List<Employee> allEmployees = getAllEmployees();
        if (allEmployees != null) {
            return allEmployees.stream().filter(e -> e.getEmployeeName().contains(name)).toList();
        }
        return null;
    }

    public Employee getEmployeeById(String id) {
        var employee = client.get().uri("/" + id).retrieve().bodyToMono(EmployeeResponse.class).block();
        return Optional.ofNullable(employee).map(EmployeeResponse::getData).orElse(null);
    }

    public Integer getHighestSalary() {
        var employees = getAllEmployees();
        return Optional.ofNullable(employees).flatMap(e -> e.stream().max(Comparator.comparingInt(Employee::getEmployeeSalary)).map(Employee::getEmployeeSalary)).orElse(null);
    }

    public List<String> getTopTenHighestEarners() {
        var employees = getAllEmployees();
        if (employees != null) {
            return employees.stream().sorted(Comparator.comparingInt(Employee::getEmployeeSalary).reversed()).limit(10).map(Employee::getEmployeeName).toList();
        }
        return null;
    }

    public Employee createEmployee(EmployeeInput input) {
        EmployeeResponse response = client.post().bodyValue(input).retrieve().bodyToMono(EmployeeResponse.class).block();
        if (response != null) {
            return response.getData();
        }
        return null;
    }

    public String deleteById(String id) {
        Employee employee = getEmployeeById(id);
        if (employee != null) {
            EmployeeDeleteResponse deleteResponse = client.method(HttpMethod.DELETE).bodyValue(new EmployeeDelete(employee.getEmployeeName())).retrieve().bodyToMono(EmployeeDeleteResponse.class).block();
            if (deleteResponse != null && Boolean.TRUE.equals(deleteResponse.data())) {
                return employee.getEmployeeName();
            }
        }
        return null;
    }
}
