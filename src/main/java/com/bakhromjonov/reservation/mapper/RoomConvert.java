package com.bakhromjonov.reservation.mapper;

import com.bakhromjonov.reservation.dto.RoomDTO;
import com.bakhromjonov.reservation.entity.Room;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomConvert {

    public RoomDTO entityToDto(Room room) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(room, RoomDTO.class);
    }

    public List<RoomDTO> entityToDto(List<Room> rooms) {
        return rooms.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public Collection<RoomDTO> entityToDto(Collection<Room> rooms) {
        return rooms.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public Room dtoToEntity(RoomDTO roomDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(roomDTO, Room.class);
    }

    public List<Room> dtoToEntity(List<RoomDTO> roomDTOS) {
        return roomDTOS.stream().map(this::dtoToEntity).collect(Collectors.toList());
    }
}
