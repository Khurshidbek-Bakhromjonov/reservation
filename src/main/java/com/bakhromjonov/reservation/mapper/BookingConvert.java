package com.bakhromjonov.reservation.mapper;

import com.bakhromjonov.reservation.dto.BookingDTO;
import com.bakhromjonov.reservation.entity.Booking;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingConvert {

    public BookingDTO entityToDto(Booking booking) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(booking, BookingDTO.class);
    }

    public List<BookingDTO> entityToDto(List<Booking> bookings) {
        return bookings.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public Collection<BookingDTO> entityToDto(Collection<Booking> bookings) {
        return bookings.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public Booking dtoToEntity(BookingDTO bookingDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(bookingDTO, Booking.class);
    }

    public List<Booking> dtoToEntity(List<BookingDTO> bookingDTOS) {
        return bookingDTOS.stream().map(this::dtoToEntity).collect(Collectors.toList());
    }
}
