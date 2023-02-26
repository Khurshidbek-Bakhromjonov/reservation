package com.bakhromjonov.reservation.service;

import com.bakhromjonov.reservation.dto.BookingDTO;
import com.bakhromjonov.reservation.dto.RoomDTO;
import com.bakhromjonov.reservation.dto.UserDTO;
import com.bakhromjonov.reservation.entity.Booking;
import com.bakhromjonov.reservation.entity.Room;
import com.bakhromjonov.reservation.entity.User;
import com.bakhromjonov.reservation.exception.NotFoundException;
import com.bakhromjonov.reservation.mapper.BookingConvert;
import com.bakhromjonov.reservation.mapper.RoomConvert;
import com.bakhromjonov.reservation.mapper.UserConvert;
import com.bakhromjonov.reservation.repositorty.BookingRepository;
import com.bakhromjonov.reservation.repositorty.DepartmentRepository;
import com.bakhromjonov.reservation.repositorty.RoomRepository;
import com.bakhromjonov.reservation.utils.FormClass;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final DepartmentRepository departmentRepository;
    private final RoomConvert roomConvert;
    private final BookingConvert bookingConvert;
    private final UserConvert userConvert;


    public List<RoomDTO> listAll() {
        return roomConvert.entityToDto(roomRepository.findAll());
    }

    public List<RoomDTO> roomsAvailable() {
        List<Room> rooms = roomRepository.findAll();
        List<Room> roomsAvailable = new ArrayList<>();
        for (Room room : rooms) {
            if (!room.isReserved())
                roomsAvailable.add(room);
        }
        return roomConvert.entityToDto(roomsAvailable);
    }

    public RoomDTO getById(Long id) throws NotFoundException {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isEmpty()) throw new NotFoundException("Room Not Available");
        return roomConvert.entityToDto(room.get());
    }

    public List<RoomDTO> saveRooms(List<RoomDTO> roomDTOS) {
        List<Room> rooms = roomConvert.dtoToEntity(roomDTOS);
        rooms = roomRepository.saveAll(rooms);
        return roomConvert.entityToDto(rooms);
    }

    public Collection<UserDTO> getUsersByRoom(Long roomId) throws NotFoundException {
        RoomDTO roomDTO = getById(roomId);
        Room room = roomConvert.dtoToEntity(roomDTO);
        Collection<Booking> bookings = room.getBookings();
        List<User> users = new ArrayList<>();
        for (Booking booking : bookings) {
            users.add(booking.getUser());
        }
        return userConvert.entityToDto(users);
    }

    public RoomDTO getRoomByName(String name) throws NotFoundException {
        if (name.isEmpty()) throw new NotFoundException("Insert a name to find room");
        if (Objects.isNull(roomRepository.findByName(name))) throw new NotFoundException("Room not found");
        return roomConvert.entityToDto(roomRepository.findByName(name));
    }

    public RoomDTO save(FormClass.RoomForm form) {
        Room room = new Room();
        room.setReserved(false);
        room.setName(form.getName());
        room.setDepartment(departmentRepository.getById(form.getId()));
        room = roomRepository.save(room);
        return roomConvert.entityToDto(room);
    }

    public List<RoomDTO> findAvailable(String start, String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        List<Room> roomsAvailable = roomRepository.findMeetingRoomAvailable(startDate, endDate);
        return roomConvert.entityToDto(roomsAvailable);
    }

    public Collection<BookingDTO> bookingsByRoom(Long roomId) throws NotFoundException {
        RoomDTO roomDTO = getById(roomId);
        Room room = roomConvert.dtoToEntity(roomDTO);
        Collection<Booking> bookings = room.getBookings();
        return bookingConvert.entityToDto(bookings);
    }

    public RoomDTO mostBookedRoom() {
        List<RoomDTO> roomDTOS = listAll();
        return roomDTOS.stream().max(Comparator.comparing(RoomDTO::getCounter)).get();
    }

    public RoomDTO getLastReservedRoom() {
        List<Booking> bookings = bookingRepository.findAll();
        Booking booking = bookings.get(bookings.size() - 1);
        return roomConvert.entityToDto(booking.getRoom());
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    public RoomDTO updateRoom(Long id, RoomDTO roomDTO) throws NotFoundException {
        Room room = roomRepository.findById(id).orElseThrow(() -> new NotFoundException("Room Not Found"));
        if (Objects.nonNull(roomDTO.getName()) && !"".equalsIgnoreCase(roomDTO.getName()))
            room.setName(roomDTO.getName());

        roomRepository.save(room);
        return roomConvert.entityToDto(room);
    }
}
