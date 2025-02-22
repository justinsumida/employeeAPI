package com.reliaquest.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.reliaquest.model.Employee;
import com.reliaquest.model.EmployeeDelete;
import com.reliaquest.model.EmployeeInput;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController implements IEmployeeController<Employee, EmployeeInput>{
    private final WebClient client = WebClient.builder().baseUrl("http://localhost:8112/api/v1/employee").build();

    @Override
    @GetMapping("/getAllEmployees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        var res = client.get().retrieve().bodyToMono(EmployeeListResponse.class).block();
        if (res != null) {
            return new ResponseEntity<>(res.getData(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @GetMapping("/getEmployeesByName/{searchString}")
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) {
        var res = client.get().retrieve().bodyToMono(EmployeeListResponse.class).block();
        if (res != null) {
            List<Employee> employeesByName = res.getData().stream().filter(e -> e.getEmployeeName().contains(searchString)).toList();
            return new ResponseEntity<>(employeesByName, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        var res = client.get().uri("/" + id).retrieve().bodyToMono(EmployeeResponse.class).block();
        if (res != null) {
            return new ResponseEntity<>(res.getData(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //TODO: unit test this by getting entire list, and then validating that the highest is indeed the highest here.
    @Override
    @GetMapping("/getHighestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        var res = client.get().retrieve().bodyToMono(EmployeeListResponse.class).block();
        if (res != null) { //todo: flip the conditions
            Integer highestSalary = res.getData().stream().max(Comparator.comparingInt(Employee::getEmployeeSalary)).map(Employee::getEmployeeSalary).orElse(null);
            return new ResponseEntity<>(highestSalary, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @GetMapping("/getTopTenHighestEarningEmployees")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        var res = client.get().retrieve().bodyToMono(EmployeeListResponse.class).block();
        if (res != null) {
            List<String> names = res.getData().stream().sorted(Comparator.comparingInt(Employee::getEmployeeSalary).reversed()).limit(10).map(Employee::getEmployeeName).toList();
            return new ResponseEntity<>(names, HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeInput employeeInput) {
        var res = client.post().bodyValue(employeeInput).retrieve().bodyToMono(EmployeeResponse.class).block();
        if (res != null) {
            return new ResponseEntity<>(res.getData(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        var res = getEmployeeById(id);
        if (res.getBody() != null) {
            var employee = res.getBody();
            var deleteRes = client.method(HttpMethod.DELETE).bodyValue(new EmployeeDelete(employee.getEmployeeName())).retrieve().bodyToMono(EmployeeDeleteResponse.class).block();
            if (Boolean.TRUE.equals(deleteRes.data())) {
                return new ResponseEntity<>(employee.getEmployeeName(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

