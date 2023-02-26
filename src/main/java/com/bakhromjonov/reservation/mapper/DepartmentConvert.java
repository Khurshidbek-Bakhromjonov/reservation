package com.bakhromjonov.reservation.mapper;

import com.bakhromjonov.reservation.dto.DepartmentDTO;
import com.bakhromjonov.reservation.entity.Department;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DepartmentConvert {

    public DepartmentDTO entityToDto(Department department) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(department, DepartmentDTO.class);
    }

    public List<DepartmentDTO> entityToDto(List<Department> departments) {
        return departments.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public Department dtoToEntity(DepartmentDTO departmentDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(departmentDTO, Department.class);
    }

    public List<Department> dtoToEntity(List<DepartmentDTO> departmentDTOS) {
        return departmentDTOS.stream().map(this::dtoToEntity).collect(Collectors.toList());
    }
}
