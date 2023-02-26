package com.bakhromjonov.reservation.service;

import com.bakhromjonov.reservation.dto.RoleDTO;
import com.bakhromjonov.reservation.entity.Role;
import com.bakhromjonov.reservation.exception.NotFoundException;
import com.bakhromjonov.reservation.mapper.RoleConvert;
import com.bakhromjonov.reservation.repositorty.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleConvert roleConvert;


    public List<RoleDTO> listAll() {
        return roleConvert.entityToDto(roleRepository.findAll());
    }

    public RoleDTO getById(Long id) throws NotFoundException {
        Role role = roleRepository.findById(id).orElseThrow(() -> new NotFoundException("Role Not Found"));
        return roleConvert.entityToDto(role);
    }

    public List<RoleDTO> saveRoles(List<RoleDTO> roleDTOS) {
        List<Role> roles = roleConvert.dtoToEntity(roleDTOS);
        roles = roleRepository.saveAll(roles);
        return roleConvert.entityToDto(roles);
    }

    public RoleDTO getRoleByName(String name) {
        return roleConvert.entityToDto(roleRepository.findByRoleName(name));
    }

    public RoleDTO save(RoleDTO roleDTO) {
        Role role = roleConvert.dtoToEntity(roleDTO);
        role = roleRepository.save(role);
        return roleConvert.entityToDto(role);
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
