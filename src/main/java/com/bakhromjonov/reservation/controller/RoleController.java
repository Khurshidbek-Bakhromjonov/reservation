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
@RequestMapping("/api/role")

public class RoleController {
    private final RoleService service;
    private final UserService userService;

    @GetMapping("/all")
    public List<RoleDTO> findAllRoles() {
        return service.listAll();
    }

    @GetMapping("/find-id/{id}")
    public RoleDTO findRoleById(@PathVariable Long id) throws NotFoundException {
        return service.getById(id);
    }

    @GetMapping("/find-name/{name}")
    public RoleDTO findRoleByName(@PathVariable String name) {
        return service.getRoleByName(name);
    }

    @PostMapping("/save")
    public RoleDTO addRole(@RequestBody RoleDTO roleDTO) {
        return service.save(roleDTO);
    }

    @PostMapping("/save/all")
    public List<RoleDTO> addRoles(@RequestBody List<RoleDTO> roles) {
        return service.saveRoles(roles);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteRole(@PathVariable Long id) {
        service.deleteRole(id);
        return "Deleted successfully";
    }

    @PostMapping("/add-to-user")
    public ResponseEntity<?> addRoleToUser(@RequestBody FormClass.RoleToUserForm form) {
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

}
