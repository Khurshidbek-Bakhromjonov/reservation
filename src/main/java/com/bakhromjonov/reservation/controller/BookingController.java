package com.bakhromjonov.reservation.controller;

import com.bakhromjonov.reservation.dto.BookingDTO;
import com.bakhromjonov.reservation.dto.UserDTO;
import com.bakhromjonov.reservation.exception.NotFoundException;
import com.bakhromjonov.reservation.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/booking")
public class BookingController {
    private final BookingService service;

    @GetMapping("/all")
    public List<BookingDTO> findAllBookings() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public BookingDTO findBookingById(@PathVariable Long id) throws NotFoundException {
        return service.getById(id);
    }

    @GetMapping("/{id}/user")
    public ResponseEntity<UserDTO> GetUserByBookId(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok().body(service.getUsersByBookingId(id));
    }

    @GetMapping("/find-code/{code}")
    public BookingDTO findBookingByCode(@PathVariable String code) {
        return service.getBookingByCode(code);
    }

    @PostMapping("/save")
    public BookingDTO addBooking(@Valid @RequestBody BookingDTO bookingDTO) throws NotFoundException {
        return service.save(bookingDTO);
    }

    @GetMapping("/all/room")
    public ResponseEntity<Collection<BookingDTO>> FetchBookingsByRoomName(@RequestBody String name) throws NotFoundException {

        return ResponseEntity.ok().body(service.getAllByRoomName(name));
    }


    @GetMapping("/next")
    public ResponseEntity<String> getNextBooking() throws NotFoundException {
        return ResponseEntity.ok().body(service.nextBooking());
    }

    @PostMapping("/save/all")
    public List<BookingDTO> addBookings(@RequestBody List<BookingDTO> bookingDTOS) {
        return service.saveDepartments(bookingDTOS);
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<String> confirmBooking(@RequestBody boolean confirmed, @PathVariable Long id) throws NotFoundException {
        service.confirmBooking(id, confirmed);
        if (confirmed)
            return ResponseEntity.ok().body("confirmed");
        else {
            return ResponseEntity.ok().body("not confirmed!");

        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok().body(service.deleteBooking(id));
    }


}
