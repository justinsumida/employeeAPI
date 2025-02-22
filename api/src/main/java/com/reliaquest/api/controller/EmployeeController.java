package com.reliaquest.api.controller;

import com.reliaquest.api.service.EmployeeService;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeInput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController implements IEmployeeController<Employee, EmployeeInput>{
    private final EmployeeService employeeService;

    @Override
    @GetMapping("/getAllEmployees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return response(employeeService.getAllEmployees());
    }

    @Override
    @GetMapping("/getEmployeesByName/{searchString}")
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) {
        return response(employeeService.getEmployeesByName(searchString));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        return response(employeeService.getEmployeeById(id));
    }

    @Override
    @GetMapping("/getHighestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return response(employeeService.getHighestSalary());
    }

    @Override
    @GetMapping("/getTopTenHighestEarningEmployees")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return response(employeeService.getTopTenHighestEarners());
    }

    @Override
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeInput employeeInput) {
        return response(employeeService.createEmployee(employeeInput));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        return response(employeeService.deleteById(id));
    }

    private <T> ResponseEntity<T> response(T response) {
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

