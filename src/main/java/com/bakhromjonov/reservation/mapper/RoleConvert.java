package com.bakhromjonov.reservation.mapper;

import com.bakhromjonov.reservation.dto.RoleDTO;
import com.bakhromjonov.reservation.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleConvert {

    public RoleDTO entityToDto(Role role) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(role, RoleDTO.class);
    }

    public List<RoleDTO> entityToDto(List<Role> roles) {
        return roles.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public Role dtoToEntity(RoleDTO roleDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(roleDTO, Role.class);
    }

    public List<Role> dtoToEntity(List<RoleDTO> roleDTOS) {
        return roleDTOS.stream().map(this::dtoToEntity).collect(Collectors.toList());
    }
}
