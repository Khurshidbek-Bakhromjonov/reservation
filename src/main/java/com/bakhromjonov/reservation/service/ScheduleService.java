package com.bakhromjonov.reservation.service;

import com.bakhromjonov.reservation.entity.Booking;
import com.bakhromjonov.reservation.repositorty.BookingRepository;
import com.bakhromjonov.reservation.repositorty.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final BookingRepository bookingRepository;
    private final EmailSenderService emailSenderService;
    private final RoomRepository roomRepository;


    @Scheduled(cron = "0 0/5 * * * *")
    public void job1() {
        LocalDateTime currentDate = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findAll();
        List<Booking> activeBookings = new ArrayList<>();

        for (Booking booking : bookings) {
            if (booking.getEndDate().plusMinutes(5).isAfter(currentDate))
                activeBookings.add(booking);
        }

        for (Booking activeBooking : activeBookings) {
            if (activeBooking.getEndDate().truncatedTo(ChronoUnit.SECONDS).isEqual(currentDate.truncatedTo(ChronoUnit.SECONDS))) {
                String body = "Your room " + activeBooking.getRoom().getName() + " is now free, booking " + activeBooking.getCode() + " has ended.";
                String to = activeBooking.getUser().getEmail();
                String subject = "Reservation completed";
                try {
                    emailSenderService.sendEmail(to, body, subject);
                } catch (MailException mailException) {
                    mailException.printStackTrace();
                }
                activeBooking.getRoom().setReserved(false);
                roomRepository.save(activeBooking.getRoom());
            }
        }
        System.out.println("Ran Job at " + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

    @Scheduled(cron = "0 0/5 * * * *")
    public void job2() {
        LocalDateTime currentDate = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findAll();
        List<Booking> activeBookings = new ArrayList<>();

        for (Booking booking : bookings) {
            if (booking.getStartDate().plusMinutes(5).isAfter(currentDate)) {
                activeBookings.add(booking);
                System.out.println(booking.getRoom().getName());
            }
        }

        for (Booking activeBooking : activeBookings) {
            if (activeBooking.getStartDate().truncatedTo(ChronoUnit.SECONDS)
                    .isEqual(currentDate.truncatedTo(ChronoUnit.SECONDS)) && activeBooking.isConfirmed()) {
                System.out.println(activeBooking.getRoom().getName());
                activeBooking.getRoom().setReserved(true);
                roomRepository.save(activeBooking.getRoom());
            }
        }
        System.out.println("Ran Job 2 at " + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }
}
