package com.bakhromjonov.reservation.controller;

import com.bakhromjonov.reservation.dto.BookingDTO;
import com.bakhromjonov.reservation.dto.RoomDTO;
import com.bakhromjonov.reservation.dto.UserDTO;
import com.bakhromjonov.reservation.exception.NotFoundException;
import com.bakhromjonov.reservation.service.RoomService;
import com.bakhromjonov.reservation.utils.FormClass;
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
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;


    @GetMapping
    public ResponseEntity<List<RoomDTO>> findAllRooms() {
        return ResponseEntity.ok().body(roomService.listAll());
    }

    @GetMapping("/available")
    public ResponseEntity<List<RoomDTO>> findAvailableRooms() {
        return ResponseEntity.ok().body(roomService.roomsAvailable());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> findRoomById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok().body(roomService.getById(id));
    }

    @GetMapping("/name")
    public ResponseEntity<RoomDTO> findRoomByName(@RequestParam String name) throws NotFoundException {
        return ResponseEntity.ok().body(roomService.getRoomByName(name));
    }

    @GetMapping("/availability")
    public ResponseEntity<List<RoomDTO>> findAvailableRoomsByDate(@RequestParam String start,
                                                                  @RequestParam String end) {
        return ResponseEntity.ok().body(roomService.findAvailable(start, end));
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<Collection<BookingDTO>> getBookingsByRoom(@PathVariable("id") Long roomId) throws NotFoundException {
        return ResponseEntity.ok().body(roomService.bookingsByRoom(roomId));
    }

    @GetMapping("/bookings/latest")
    public ResponseEntity<RoomDTO> fetchLatestReservedRoom() {
        return ResponseEntity.ok().body(roomService.getLastReservedRoom());
    }

    @GetMapping("most-booked")
    public ResponseEntity<RoomDTO> getMostBookedRoom() {
        return ResponseEntity.ok().body(roomService.mostBookedRoom());
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<Collection<UserDTO>> getUsersByRoom(@PathVariable("id") Long roomId) throws NotFoundException {
        return ResponseEntity.ok().body(roomService.getUsersByRoom(roomId));
    }

    @PostMapping
    public ResponseEntity<RoomDTO> addRoom(@RequestBody FormClass.RoomForm form) {
        RoomDTO result = roomService.save(form);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/rooms").toUriString());
        return ResponseEntity.created(uri).body(result);
    }

    @PostMapping
    public ResponseEntity<List<RoomDTO>> addRooms(@RequestBody List<RoomDTO> roomDTOS) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/rooms").toUriString());
        return ResponseEntity.created(uri).body(roomService.saveRooms(roomDTOS));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomDTO> updateRoom(@PathVariable Long id,
                                              @RequestBody RoomDTO roomDTO) throws NotFoundException {
        RoomDTO result = roomService.updateRoom(id, roomDTO);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/rooms/{id}").toUriString());
        return ResponseEntity.created(uri).body(result);
    }

    @DeleteMapping("/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return "Deleted successfully";
    }
}
