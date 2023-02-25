package com.bakhromjonov.reservation.repositorty;

import com.bakhromjonov.reservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByUsername(String username);

    List<User> findByRolesId(Long id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
