package com.bakhromjonov.reservation.repositorty;

import com.bakhromjonov.reservation.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(String role);
}