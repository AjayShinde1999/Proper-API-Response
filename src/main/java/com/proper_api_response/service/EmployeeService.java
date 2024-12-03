package com.proper_api_response.service;

import com.proper_api_response.dto.EmployeeDto;
import com.proper_api_response.entity.Employee;
import com.proper_api_response.exception.ResourceNotFoundException;
import com.proper_api_response.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;
import com.proper_api_response.utils.PageResponse;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employeeEntity = modelMapper.map(employeeDto, Employee.class);
        Employee savedEmployee = employeeRepository.save(employeeEntity);
        return modelMapper.map(savedEmployee, EmployeeDto.class);
    }

    public PageResponse getAllEmployee(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Employee> pageList = employeeRepository.findAll(pageable);
        List<Employee> employeeList = pageList.getContent();
        List<EmployeeDto> contents = employeeList.stream().map(employee -> modelMapper.map(employee, EmployeeDto.class)).collect(Collectors.toList());
        PageResponse pageResponse = PageResponse.<EmployeeDto>builder()
                .content(contents)
                .pageNumber(pageList.getNumber())
                .pageSize(pageList.getSize())
                .totalElements(pageList.getTotalElements())
                .totalPages(pageList.getTotalPages())
                .lastPage(pageList.isLast())
                .build();
        return pageResponse;
    }

    public EmployeeDto updateEmployee(EmployeeDto employeeDto, long id) {
        Employee existingEmployee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id :" + id));

        employeeDto.setId(existingEmployee.getId());
        modelMapper.map(employeeDto, existingEmployee);
        log.info("id {}", employeeDto.getId());
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return modelMapper.map(updatedEmployee, EmployeeDto.class);

//        existingEmployee.setName(employeeDto.getName());
//        existingEmployee.setEmail(employeeDto.getEmail());
//        Employee updatedEmployee = employeeRepository.save(existingEmployee);
//        return modelMapper.map(updatedEmployee,EmployeeDto.class);
    }

    public EmployeeDto updateField(Map<String, Object> updates, long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id : " + id));
        updates.forEach((field, value) -> {
            Field fieldToUpdate = ReflectionUtils.findRequiredField(Employee.class, field);
            fieldToUpdate.setAccessible(true);
            ReflectionUtils.setField(fieldToUpdate, employee, value);
        });

        Employee updatedEmployee = employeeRepository.save(employee);
        return modelMapper.map(updatedEmployee, EmployeeDto.class);
    }

}
