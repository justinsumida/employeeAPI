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
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Override
    @GetMapping("/getEmployeesByName/{searchString}")
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) {
        List<Employee> employees = employeeService.getEmployeesByName(searchString);
        if (employees == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    //TODO: unit test this by getting entire list, and then validating that the highest is indeed the highest here.
    @Override
    @GetMapping("/getHighestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        Integer highest = employeeService.getHighestSalary();
        if (highest == null) { //todo: flip the conditions
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(highest, HttpStatus.OK);
    }

    @Override
    @GetMapping("/getTopTenHighestEarningEmployees")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<String> names = employeeService.getTopTenHighestEarners();
        if (names == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<>(names, HttpStatus.OK);
    }

    @Override
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeInput employeeInput) {
        Employee newEmployee = employeeService.createEmployee(employeeInput);
        if (newEmployee == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(newEmployee, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        String employeeNameDeleted = employeeService.deleteById(id);
        if (employeeNameDeleted == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(employeeNameDeleted, HttpStatus.OK);
    }
}

