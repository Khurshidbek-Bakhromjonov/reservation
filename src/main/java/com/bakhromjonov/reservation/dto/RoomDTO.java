package com.bakhromjonov.reservation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Collection;

@Data
public class RoomDTO {

    private Long id;
    private String name;
    boolean reserved = false;
    int counter = 0;
    private DepartmentDTO department;

    @JsonIgnore
    private Collection<BookingDTO> bookings;
}
