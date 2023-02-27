package com.bakhromjonov.reservation.service;

import com.bakhromjonov.reservation.dto.DepartmentDTO;
import com.bakhromjonov.reservation.dto.RoomDTO;
import com.bakhromjonov.reservation.entity.Department;
import com.bakhromjonov.reservation.entity.Room;
import com.bakhromjonov.reservation.exception.NotFoundException;
import com.bakhromjonov.reservation.mapper.DepartmentConvert;
import com.bakhromjonov.reservation.mapper.RoomConvert;
import com.bakhromjonov.reservation.repositorty.DepartmentRepository;
import com.bakhromjonov.reservation.repositorty.RoomRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentConvert departmentConvert;
    private final RoomConvert roomConvert;
    private final RoomRepository roomRepository;


    public List<DepartmentDTO> listAll() {
        return departmentConvert.entityToDto(departmentRepository.findAll());
    }

    public DepartmentDTO getById(Long id) throws NotFoundException {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new NotFoundException("Department Not Found"));
        return departmentConvert.entityToDto(department);
    }

    public Collection<RoomDTO> roomsByDepartmentId(Long id) throws NotFoundException {
        DepartmentDTO departmentDTO = getById(id);
        Department department = departmentConvert.dtoToEntity(departmentDTO);
        Collection<Room> rooms = department.getRoom();
        return roomConvert.entityToDto(rooms);
    }

    public DepartmentDTO getDepartmentByName(String name) throws NotFoundException {
        if (name.isEmpty()) throw new NotFoundException("Insert a name to find department");
        if (Objects.isNull(departmentRepository.findByName(name))) throw new NotFoundException("Department not found");
        return departmentConvert.entityToDto(departmentRepository.findByName(name));
    }

    public DepartmentDTO save(DepartmentDTO departmentDTO) {
        Department department = departmentConvert.dtoToEntity(departmentDTO);
        department = departmentRepository.save(department);
        return departmentConvert.entityToDto(department);
    }

    public List<DepartmentDTO> saveDepartments(List<DepartmentDTO> departmentDTOS) {
        List<Department> departments = departmentConvert.dtoToEntity(departmentDTOS);
        departments = departmentRepository.saveAll(departments);
        return departmentConvert.entityToDto(departments);
    }

    public String deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
        return "Deleted successfully";
    }

    public DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO) throws NotFoundException {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new NotFoundException("Department Not Found"));
        if (Objects.nonNull(departmentDTO.getName()) && !"".equalsIgnoreCase(departmentDTO.getName()))
            department.setName(departmentDTO.getName());
        departmentRepository.save(department);
        return departmentConvert.entityToDto(department);
    }
}
