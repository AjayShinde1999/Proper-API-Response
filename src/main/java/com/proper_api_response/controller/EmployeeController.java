package com.proper_api_response.controller;

import com.proper_api_response.dto.EmployeeDto;
import com.proper_api_response.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.proper_api_response.utils.PageResponse;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeDto> saveEmployee(@RequestBody EmployeeDto employeeDto) {
        return new ResponseEntity<>(employeeService.createEmployee(employeeDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResponse> getAllEmployees(@RequestParam(defaultValue = "0", required = false) int pageNumber,
                                                        @RequestParam(defaultValue = "5", required = false) int pageSize,
                                                        @RequestParam(defaultValue = "id", required = false) String sortBy,
                                                        @RequestParam(defaultValue = "asc", required = false) String sortDir
    ) {
        return ResponseEntity.ok(employeeService.getAllEmployee(pageNumber, pageSize, sortBy,sortDir));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@RequestBody EmployeeDto employeeDto, @PathVariable long id) {
        return ResponseEntity.ok(employeeService.updateEmployee(employeeDto, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateField(@RequestBody Map<String, Object> updates, @PathVariable long id) {
        return ResponseEntity.ok(employeeService.updateField(updates, id));
    }

}
