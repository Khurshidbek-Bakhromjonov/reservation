package com.bakhromjonov.reservation.service;

import com.bakhromjonov.reservation.dto.BookingDTO;
import com.bakhromjonov.reservation.dto.UserDTO;
import com.bakhromjonov.reservation.entity.Booking;
import com.bakhromjonov.reservation.entity.Room;
import com.bakhromjonov.reservation.entity.User;
import com.bakhromjonov.reservation.exception.NotFoundException;
import com.bakhromjonov.reservation.mapper.BookingConvert;
import com.bakhromjonov.reservation.repositorty.BookingRepository;
import com.bakhromjonov.reservation.repositorty.RoomRepository;
import com.bakhromjonov.reservation.repositorty.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingConvert bookingConvert;
    private final RoomRepository roomRepository;


    public List<BookingDTO> listAll() {
        return bookingConvert.entityToDto(bookingRepository.findAll());
    }

    public void deleteByCode(String code) {
        bookingRepository.deleteByCode(code);
    }

    public BookingDTO getById(Long id) throws NotFoundException {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Reservation Not Found"));
        return bookingConvert.entityToDto(booking);
    }

    public BookingDTO getBookingByCode(String code) {
        return bookingConvert.entityToDto(bookingRepository.findByCode(code));
    }

    public BookingDTO bookRoom(String name, LocalDateTime startDate, LocalDateTime endDate) {
        Booking booking = new Booking();
        booking.setRoom(roomRepository.findByName(name));
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setCode(UUID.randomUUID().toString());
        return bookingConvert.entityToDto(booking);
    }

    public String nextBooking() {
        List<Booking> bookings = bookingRepository.findAll();
        Booking next = new Booking();
        long minValue = 100000000;
        for (Booking booking : bookings) {
            if (booking.getStartDate().isAfter(LocalDateTime.now()) && booking.isConfirmed()) {
                long duration = Duration.between(LocalDateTime.now(), booking.getStartDate()).toMinutes();
                if (duration < minValue) {
                    minValue = duration;
                    next = booking;
                }
                System.out.println(duration);
            }
        }
        return next.getCode();
    }

    public Collection<BookingDTO> getAllByRoomName(String name) throws NotFoundException {
        Room room = roomRepository.findByName(name);
        if (Objects.isNull(room))
            throw new NotFoundException("Room Not Found");
        else {
            Collection<Booking> bookings = room.getBookings();
            return bookingConvert.entityToDto(bookings);
        }
    }

    public UserDTO getUsersByBookingId(Long id) throws NotFoundException {
        BookingDTO bookingDTO = getById(id);
        return bookingDTO.getUser();
    }

    public BookingDTO save(BookingDTO bookingDTO) throws NotFoundException {
        Booking booking = bookingConvert.dtoToEntity(bookingDTO);
        booking.setCode(UUID.randomUUID().toString());

        Room room = roomRepository.checkAvailability(booking.getStartDate(), booking.getEndDate(), booking.getRoom().getId());
        if (Objects.isNull(room)) {
            booking = bookingRepository.save(booking);
            return bookingConvert.entityToDto(booking);
        } else
            throw new NotFoundException("Room already in use");
    }


    public List<BookingDTO> saveBookings(List<BookingDTO> bookingDTOS) {
        List<Booking> bookings = bookingConvert.dtoToEntity(bookingDTOS);
        bookings = bookingRepository.saveAll(bookings);
        return bookingConvert.entityToDto(bookings);
    }

    public void confirmBooking(Long id, boolean isConfirmed) throws NotFoundException {
        BookingDTO bookingDTO = getById(id);
        Booking booking = bookingConvert.dtoToEntity(bookingDTO);
        booking.setConfirmed(isConfirmed);
        bookingRepository.save(booking);
        booking.getRoom().setCounter(booking.getRoom().getCounter() + 1);
        roomRepository.save(booking.getRoom());
    }

    public List<BookingDTO> saveDepartments(List<BookingDTO> bookingDTOS) {
        List<Booking> bookings = bookingConvert.dtoToEntity(bookingDTOS);
        bookings = bookingRepository.saveAll(bookings);
        return bookingConvert.entityToDto(bookings);
    }

    public String deleteBooking(Long id) throws NotFoundException {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Reservation Not Found"));
        bookingRepository.deleteById(id);
        return "Deleted successfully";
    }
}
