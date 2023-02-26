package com.bakhromjonov.reservation.controller;

import com.bakhromjonov.reservation.dto.BookingDTO;
import com.bakhromjonov.reservation.dto.UserDTO;
import com.bakhromjonov.reservation.exception.NotFoundException;
import com.bakhromjonov.reservation.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<BookingDTO> findAllBookings() {
        return bookingService.listAll();
    }

    @GetMapping("/{id}")
    public BookingDTO findBookingById(@PathVariable Long id) throws NotFoundException {
        return bookingService.getById(id);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<UserDTO> getUsersByBookingId(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok().body(bookingService.getUsersByBookingId(id));
    }

    @GetMapping("/{code}")
    public BookingDTO findBookingByCode(@PathVariable String code) {
        return bookingService.getBookingByCode(code);
    }

    @PostMapping
    public BookingDTO addBooking(@Valid @RequestBody BookingDTO bookingDTO) throws NotFoundException {
        return bookingService.save(bookingDTO);
    }

    @GetMapping("/rooms")
    public ResponseEntity<Collection<BookingDTO>> fetchBookingsByRoomName(@RequestBody String name) throws NotFoundException {
        return ResponseEntity.ok().body(bookingService.getAllByRoomName(name));
    }

    @GetMapping("/next-booking")
    public ResponseEntity<String> getNextBooking() {
        return ResponseEntity.ok().body(bookingService.nextBooking());
    }

    @PostMapping
    public List<BookingDTO> addBookings(@RequestBody List<BookingDTO> bookingDTOS) {
        return bookingService.saveBookings(bookingDTOS);
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<String> confirmBooking(@RequestBody boolean confirmed, @PathVariable Long id) throws NotFoundException {
        bookingService.confirmBooking(id, confirmed);
        return confirmed ? ResponseEntity.ok().body("confirmed") : ResponseEntity.ok().body("not confirmed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok().body(bookingService.deleteBooking(id));
    }
}
