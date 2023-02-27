package com.bakhromjonov.reservation.controller;

import com.bakhromjonov.reservation.dto.BookingDTO;
import com.bakhromjonov.reservation.dto.UserDTO;
import com.bakhromjonov.reservation.entity.User;
import com.bakhromjonov.reservation.exception.NotFoundException;
import com.bakhromjonov.reservation.service.UserService;
import com.bakhromjonov.reservation.utils.FormClass;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        return ResponseEntity.ok().body(userService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok().body(userService.getById(id));
    }

    @GetMapping("/bookings")
    public ResponseEntity<Collection<BookingDTO>> getReservations() throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.getUserByUsername(authentication.getPrincipal().toString());
        return ResponseEntity.ok().body(userService.getReservations(loggedInUser.getId()));
    }

    @PostMapping("/book/")
    public ResponseEntity<String> bookRoom(@RequestBody FormClass.UserBookingForm form) throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.getUserByUsername(authentication.getPrincipal().toString());
        return userService.bookRoom(form, loggedInUser.getId());
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> findUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok().body(userService.getUserByEmail(email));
    }

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO result = userService.save(userDTO);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users").toUriString());
        return ResponseEntity.created(uri).body(result);
    }

    @PostMapping
    public ResponseEntity<List<UserDTO>> addUsers(@RequestBody List<UserDTO> userDTOS) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUsers(userDTOS));
    }

    @DeleteMapping("/bookings/{code}")
    public ResponseEntity<String> cancelBooking(@PathVariable String code) throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.getUserByUsername(authentication.getPrincipal().toString());
        return userService.cancelBooking(loggedInUser.getId(), code);
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<Collection<BookingDTO>> fetchBookingByUserId(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok().body(userService.getBookingsByUserId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
                                              @RequestBody UserDTO userDTO) throws NotFoundException {
        UserDTO result = userService.updateUser(id, userDTO);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{id}").toUriString());
        return ResponseEntity.created(uri).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.getUserByUsername(authentication.getPrincipal().toString());
        if (Objects.equals(loggedInUser.getId(), userService.getById(id).getId()))
            return ResponseEntity.status(405).body("cannot delete current logged in user");
        else {
            userService.deleteUser(id);
            return ResponseEntity.ok().body("User deleted!");
        }
    }
}
