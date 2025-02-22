package com.reliaquest.api;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeInput;
import com.reliaquest.api.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApiApplicationTest {
    @Autowired
    private EmployeeService employeeService;

    @Test
    void testHighestSalaryIsTheHighest() {
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees != null) {
            Integer highest = employees.stream().max(Comparator.comparingInt(Employee::getEmployeeSalary)).map(Employee::getEmployeeSalary).orElse(null);
            assertNotNull(highest);
            assertEquals(highest, employeeService.getHighestSalary());
        }
    }

    @Test
    void testHighestPaidEmployeeNameIsRight() {
        Integer highestSalary = employeeService.getHighestSalary();
        List<Employee> employees = employeeService.getAllEmployees();
        if (highestSalary != null && employees != null) {
            List<String> topTenEarners = employeeService.getTopTenHighestEarners();
            if (topTenEarners != null) {
                String name = topTenEarners.get(0);
                Employee employee = employees.stream().filter(e -> e.getEmployeeName().equals(name)).findAny().orElse(null);
                assertNotNull(employee);
                assertEquals(highestSalary, employee.getEmployeeSalary());
            }
        }
    }

    @Test
    void testCreateWorks() {
        EmployeeInput input = new EmployeeInput("Justin Sumida", 1234567, 24, "Software Engineer");
        Employee employee = employeeService.createEmployee(input);
        assertNotNull(employee);
        assertTrue(employeeService.getAllEmployees().stream().anyMatch(e -> e.getEmployeeName().equals(employee.getEmployeeName())));
    }

    @Test
    void testDeleteWorks() {
        EmployeeInput input = new EmployeeInput("Justin Sumida", 1234567, 24, "Software Engineer");
        Employee employee = employeeService.createEmployee(input);
        assertNotNull(employee);
        assertNotNull(employeeService.deleteById(employee.getId()));
    }
}
