package com.bakhromjonov.reservation.service;

import com.bakhromjonov.reservation.entity.*;
import com.bakhromjonov.reservation.repositorty.*;
import com.bakhromjonov.reservation.security.SecurityParams;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class IReservationInitService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;


    public void initRooms() {
        Room r3 = new Room();
        Stream.of("Room01", "Room02", "Room03", "Room04", "Room05", "Room06", "Room07", "Room08")
                .forEach(roomName -> {
                    Room room = new Room();
                    room.setName(roomName);
                    room.setDepartment(departmentRepository.findByName("Info"));
                    roomRepository.save(room);
                });

        r3.setName("Room09");
        r3.setDepartment(departmentRepository.findByName("Finance"));
        roomRepository.save(r3);
    }

    public void initUser() {
        User u = new User();
        User u2 = new User();
        User u3 = new User();
        u.setEmail("admin@gmail.com");
        u.setUsername("admin");
        u.setFirstName("Suvonqul");
        u.setLastName("Alishev");
        u.setPassword("$2a$10$mktizboxOpE4tRtlvvDhaeH9tbXpeMppNmoJjakS6i7UshQmLS./y");
        u.getRoles().add(roleRepository.findByRoleName(SecurityParams.ADMIN));
        u.getRoles().add(roleRepository.findByRoleName(SecurityParams.USER));
        u.setActive(true);

        //-----//

        u2.setEmail("user1@gmail.com");
        u2.setUsername("user1");
        u2.setFirstName("Quvondiq");
        u2.setLastName("Khan");
        u2.setPassword("$2a$10$mktizboxOpE4tRtlvvDhaeH9tbXpeMppNmoJjakS6i7UshQmLS./y");
        u2.getRoles().add(roleRepository.findByRoleName(SecurityParams.USER));
        u2.setActive(true);

        //-----//

        u3.setEmail("user2@gmail.com");
        u3.setUsername("user2");
        u3.setFirstName("Bonomurod");
        u3.setLastName("Aqilberdiyev");
        u3.setPassword("$2a$10$mktizboxOpE4tRtlvvDhaeH9tbXpeMppNmoJjakS6i7UshQmLS./y");
        u3.getRoles().add(roleRepository.findByRoleName(SecurityParams.USER));
        u3.setActive(true);

        userRepository.save(u);
        userRepository.save(u2);
        userRepository.save(u3);
    }

    public void initReservation() {
        String dateStart = "2023-02-26T08:00";
        String dateEnd = "2023-02-26T10:00";
        LocalDateTime dateS = LocalDateTime.parse(dateStart);
        LocalDateTime dateE = LocalDateTime.parse(dateEnd);
        Booking b = new Booking();
        Booking b2 = new Booking();
        Booking b3 = new Booking();
        b.setDescription("BookingTest1");
        b.setUser(userRepository.findByUsername("user1"));
        b.setRoom(roomRepository.findByName("Room01"));
        b.setStartDate(dateS);
        b.setEndDate(dateE);
        b.setCode(UUID.randomUUID().toString());
        b.setConfirmed(true);
        b.getRoom().setCounter(b.getRoom().getCounter() + 1);

        //----//

        String dateStart2 = "2023-02-26T14:00";
        String dateEnd2 = "2023-02-26T15:00";

        LocalDateTime dateS2 = LocalDateTime.parse(dateStart2);
        LocalDateTime dateE2 = LocalDateTime.parse(dateEnd2);
        b2.setDescription("BookingTest2");
        b2.setUser(userRepository.findByUsername("user1"));
        b2.setRoom(roomRepository.findByName("Room02"));
        b2.setCode(UUID.randomUUID().toString());
        b2.setStartDate(dateS2);
        b2.setEndDate(dateE2);
        b2.setConfirmed(true);
        b2.getRoom().setCounter(b2.getRoom().getCounter() + 1);

        //----//

        String dateStart3 = "2023-02-26T10:00";
        String dateEnd3 = "2023-02-26T13:00";
        LocalDateTime dateS3 = LocalDateTime.parse(dateStart3);
        LocalDateTime dateE3 = LocalDateTime.parse(dateEnd3);
        b3.setDescription("BookingTest3");
        b3.setUser(userRepository.findByUsername("user2"));
        b3.setRoom(roomRepository.findByName("Room01"));
        b3.setCode(UUID.randomUUID().toString());
        b3.setStartDate(dateS3);
        b3.setEndDate(dateE3);
        b3.setConfirmed(true);
        b3.getRoom().setCounter(b3.getRoom().getCounter() + 1);


        bookingRepository.save(b);
        bookingRepository.save(b2);
        bookingRepository.save(b3);
    }

    public void initDepartment() {
        Department d = new Department();
        Department d2 = new Department();
        d.setName("Info");
        d2.setName("Finance");
        departmentRepository.save(d);
        departmentRepository.save(d2);
    }

    public void initRoles() {
        Role r = new Role();
        Role r2 = new Role();
        r.setRoleName(SecurityParams.ADMIN);
        r2.setRoleName(SecurityParams.USER);
        roleRepository.save(r);
        roleRepository.save(r2);
    }
}
