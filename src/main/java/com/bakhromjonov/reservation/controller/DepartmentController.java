package com.bakhromjonov.reservation.controller;

import com.bakhromjonov.reservation.dto.DepartmentDTO;
import com.bakhromjonov.reservation.dto.RoomDTO;
import com.bakhromjonov.reservation.exception.NotFoundException;
import com.bakhromjonov.reservation.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;


    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> findAllDepartments() {
        return ResponseEntity.ok().body(departmentService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> findDepartmentById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok().body(departmentService.getById(id));
    }

    @GetMapping("/{id}/rooms")
    public ResponseEntity<Collection<RoomDTO>> fetchRoomsByDepartment(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok().body(departmentService.roomsByDepartmentId(id));
    }

    @GetMapping("/{name}")
    public ResponseEntity<DepartmentDTO> findDepartmentByName(@PathVariable String name) throws NotFoundException {
        return ResponseEntity.ok().body(departmentService.getDepartmentByName(name));
    }

    @PostMapping
    public ResponseEntity<DepartmentDTO> addDepartment(@RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO result = departmentService.save(departmentDTO);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/departments").toUriString());
        return ResponseEntity.created(uri).body(result);
    }

    @PostMapping
    public ResponseEntity<List<DepartmentDTO>> addDepartments(@RequestBody List<DepartmentDTO> departmentDTOS) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/departments").toUriString());
        return ResponseEntity.created(uri).body(departmentService.saveDepartments(departmentDTOS));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable("id") Long departmentId,
                                                          @RequestBody DepartmentDTO departmentDTO) throws NotFoundException {
        DepartmentDTO result = departmentService.updateDepartment(departmentId, departmentDTO);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/departments/{id}").toUriString());
        return ResponseEntity.created(uri).body(result);
    }

    @DeleteMapping("/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        return departmentService.deleteDepartment(id);
    }
}
