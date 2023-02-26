package com.bakhromjonov.reservation.controller;

import com.bakhromjonov.reservation.dto.RoleDTO;
import com.bakhromjonov.reservation.exception.NotFoundException;
import com.bakhromjonov.reservation.service.RoleService;
import com.bakhromjonov.reservation.service.UserService;
import com.bakhromjonov.reservation.utils.FormClass;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    private final UserService userService;


    @GetMapping
    public List<RoleDTO> findAllRoles() {
        return roleService.listAll();
    }

    @GetMapping("/{id}")
    public RoleDTO findRoleById(@PathVariable Long id) throws NotFoundException {
        return roleService.getById(id);
    }

    @GetMapping("/{name}")
    public RoleDTO findRoleByName(@PathVariable String name) {
        return roleService.getRoleByName(name);
    }

    @PostMapping
    public RoleDTO addRole(@RequestBody RoleDTO roleDTO) {
        return roleService.save(roleDTO);
    }

    @PostMapping
    public List<RoleDTO> addRoles(@RequestBody List<RoleDTO> roleDTOS) {
        return roleService.saveRoles(roleDTOS);
    }

    @DeleteMapping("/{id}")
    public String deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return "Deleted successfully";
    }

    @PostMapping("/add-to-user")
    public ResponseEntity<?> addRoleToUser(@RequestBody FormClass.RoleToUserForm form) {
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }
}
