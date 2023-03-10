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
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/department")
public class DepartmentController {
    private final
    DepartmentService service;


    @GetMapping("/all")
    public ResponseEntity<List<DepartmentDTO>> findAllDepartments() {
        return ResponseEntity.ok().body(service.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> findDepartmentById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok().body(service.getById(id));
    }

    @GetMapping("/{id}/rooms")
    public ResponseEntity<Collection<RoomDTO>> FetchRoomsByIdDepartment(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok().body(service.roomsByDepartmentId(id));
    }


    @GetMapping("/find-name/{name}")
    public ResponseEntity<DepartmentDTO> findDepartmentByName(@PathVariable String name) throws NotFoundException {

        return ResponseEntity.ok().body(service.getDepartmentByName(name));
    }

    @PostMapping("/save")
    public ResponseEntity<DepartmentDTO> CreateDepartment(@RequestBody DepartmentDTO departmentDTO) throws URISyntaxException {
        DepartmentDTO result = service.save(departmentDTO);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/department/save").toUriString());
        return ResponseEntity.created(uri).body(result);
    }

    @PostMapping("/save/all")
    public ResponseEntity<List<DepartmentDTO>> addDepartments(@RequestBody List<DepartmentDTO> departmentDTOS) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/department/save/all").toUriString());
        return ResponseEntity.created(uri).body(service.saveDepartments(departmentDTOS));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable("id") Long departmentId,
                                                          @RequestBody DepartmentDTO department) throws NotFoundException {

        DepartmentDTO result = service.updateDepartment(departmentId, department);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/department/update/{id}").toUriString());
        return ResponseEntity.created(uri).body(result);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        return service.deleteDepartment(id);
    }


}
