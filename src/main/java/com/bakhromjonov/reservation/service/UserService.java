package com.bakhromjonov.reservation.service;

import com.bakhromjonov.reservation.dto.BookingDTO;
import com.bakhromjonov.reservation.dto.UserDTO;
import com.bakhromjonov.reservation.entity.Booking;
import com.bakhromjonov.reservation.entity.Role;
import com.bakhromjonov.reservation.entity.User;
import com.bakhromjonov.reservation.exception.NotFoundException;
import com.bakhromjonov.reservation.mapper.BookingConvert;
import com.bakhromjonov.reservation.mapper.UserConvert;
import com.bakhromjonov.reservation.repositorty.BookingRepository;
import com.bakhromjonov.reservation.repositorty.RoleRepository;
import com.bakhromjonov.reservation.repositorty.UserRepository;
import com.bakhromjonov.reservation.security.SecurityParams;
import com.bakhromjonov.reservation.utils.FormClass;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final RoleRepository roleRepository;
    private final BookingService bookingService;
    private final UserConvert userConvert;
    private final BookingConvert bookingConvert;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailSenderService emailSenderService;


    public List<UserDTO> listAll() {
        return userConvert.entityToDto(userRepository.findAll());
    }

    public UserDTO getById(Long id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));
        return userConvert.entityToDto(user);
    }

    public UserDTO getUserByEmail(String email) {
        return userConvert.entityToDto(userRepository.findByEmail(email));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public ResponseEntity<?> signup(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername()))
            return ResponseEntity.badRequest().body("Error: Username is already taken!");

        if (userRepository.existsByEmail(userDTO.getEmail()))
            return ResponseEntity.badRequest().body("Email is already in use!");

        User user = userConvert.dtoToEntity(userDTO);
        user.setActive(true);
        user.setRoles(Collections.singletonList(roleRepository.findByRoleName(SecurityParams.USER)));
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        user = userRepository.save(user);
        return ResponseEntity.ok().body(userConvert.entityToDto(user));
    }

    public UserDTO save(UserDTO userDTO) {
        User user = userConvert.dtoToEntity(userDTO);
        user.setRoles(Collections.singletonList(roleRepository.findByRoleName(SecurityParams.USER)));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        user.setActive(true);
        return userConvert.entityToDto(user);
    }

    public ResponseEntity<String> bookRoom(FormClass.UserBookingForm form, Long id) throws NotFoundException {
        LocalDateTime dateStart = LocalDateTime.parse(form.getDateStart());
        LocalDateTime dateEnd = LocalDateTime.parse(form.getDateEnd());
        UserDTO userDTO = getById(id);
        BookingDTO booking = bookingService.bookRoom(form.getRoomName(), dateStart, dateEnd);
        booking.setUser(userDTO);
        booking.setDescription(form.getDescription());
        if (dateStart.isAfter(dateEnd) ||
                dateEnd.isEqual(dateStart) ||
                dateEnd.isBefore(LocalDateTime.now()) ||
                dateStart.isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Your date does not fit criteria!");
        }

        BookingDTO bookingDTO = bookingService.save(booking);

        if (!Objects.isNull(bookingDTO)) {
            Period period = Period.between(dateStart.toLocalDate(), dateEnd.toLocalDate());
            period = period.minusDays(dateEnd.toLocalTime().compareTo(dateStart.toLocalTime()) >= 0 ? 0 : 1);
            Duration duration = Duration.between(dateStart, dateEnd);
            duration = duration.minusDays(duration.toDaysPart());
            int hours = duration.toHoursPart();
            String minutes = (duration.toMinutesPart() > 0 ? (duration.toMinutesPart() + " minutes") : "");
            String days = period.getDays() > 1 ? " days" : " day";
            String isDay = (period.getDays() > 0 ? (period.getDays() + days) : "");
            List<User> users = fetchUsersByRole(SecurityParams.ADMIN);
            String body = "Reservation " + booking.getCode() + ": User " + booking.getUser().getFirstName() + " has reserved room "
                    + booking.getRoom().getName() + " with a duration of "
                    + isDay
                    + hours + " Hour" + (hours > 1 ? "s " : " ")
                    + minutes
                    + "\n\nReservation starts at  " + dateStart.format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm")) +
                    " Please confirm  booking ID : " + bookingDTO.getId() +
                    " with a  POST request at  http://localhost:8080/" +
                    bookingDTO.getId() + "/confirm?confirmed=true";
            for (User user : users) {
                try {
                    emailSenderService.sendEmail(user.getEmail(), body, "Confirm Booking!");
                } catch (MailException e) {
                    e.printStackTrace();
                }
            }
        }
        return ResponseEntity.ok().body("Added successfully");
    }

    public ResponseEntity<String> cancelBooking(Long id, String code) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));
        Collection<Booking> bookings = user.getBookings();
        Booking booking = bookingRepository.findByCode(code);
        LocalDateTime now = LocalDateTime.now();
        if (Objects.isNull(booking))
            return ResponseEntity.status(404).body("Please enter your correct reservation code!");

        if (booking.getEndDate().isBefore(now))
            return ResponseEntity.status(405).body("You cannot cancel a booking that has already ended!");

        if (bookings.contains(booking)) {

            booking.getRoom().setReserved(false);
            user.getBookings().remove(booking);
            String s = bookingService.deleteBooking(booking.getId());
            userRepository.save(user);
            List<User> users = fetchUsersByRole(SecurityParams.ADMIN);
            String body = "User " + user.getUsername() + " has cancelled the reservation "
                    + booking.getCode() +
                    " at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm")) +
                    " regarding room : " + booking.getRoom().getName();

            for (User user1 : users) {
                try {
                    emailSenderService.sendEmail(user1.getEmail(), body, "Booking cancelled!");
                } catch (MailException e) {
                    e.printStackTrace();
                }
            }
            return ResponseEntity.ok().body(s);
        } else
            return ResponseEntity.status(405).body("Error deleting reservation that is not yours");
    }

    public List<UserDTO> saveUsers(List<UserDTO> userDTOS) {
        List<User> users = userConvert.dtoToEntity(userDTOS);
        users = userRepository.saveAll(users);
        return userConvert.entityToDto(users);
    }

    public void deleteUser(Long id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));
        userRepository.delete(user);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Available"));

        if (Objects.nonNull(userDTO.getEmail()) && !"".equalsIgnoreCase(userDTO.getEmail()))
            user.setEmail(userDTO.getEmail());
        if (Objects.nonNull(userDTO.getPassword()) && !"".equalsIgnoreCase(userDTO.getPassword()))
            user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        if (Objects.nonNull(userDTO.getUsername()) && !"".equalsIgnoreCase(userDTO.getUsername()))
            user.setUsername(userDTO.getUsername());

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        userRepository.save(user);
        return userConvert.entityToDto(user);
    }

    public Collection<BookingDTO> getReservations(Long id) throws NotFoundException {
        UserDTO userDTO = getById(id);
        User user = userConvert.dtoToEntity(userDTO);
        Collection<Booking> bookings = user.getBookings();
        return bookingConvert.entityToDto(bookings);
    }

    public Collection<BookingDTO> getBookingsByUserId(Long id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));
        Collection<Booking> bookings = user.getBookings();
        return bookingConvert.entityToDto(bookings);
    }

    public void addRoleToUser(String username, String roleName) {
        User appUser = userRepository.findByUsername(username);
        Role appRole = roleRepository.findByRoleName(roleName);
        appUser.getRoles().add(appRole);
    }

    private List<User> fetchUsersByRole(String name) {
        Role role = roleRepository.findByRoleName(name);
        return userRepository.findByRolesId(role.getId());
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (Objects.isNull(user))
            throw new UsernameNotFoundException("Username not found in database");

        List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
