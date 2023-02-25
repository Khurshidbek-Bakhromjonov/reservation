package com.bakhromjonov.reservation.mapper;

import com.bakhromjonov.reservation.dto.UserDTO;
import com.bakhromjonov.reservation.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConvert {

    public UserDTO entityToDto(User user) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user, UserDTO.class);
    }

    public List<UserDTO> entityToDto(List<User> users) {
        return users.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public Collection<UserDTO> entityToDto(Collection<User> users) {
        return users.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public User dtoToEntity(UserDTO userDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userDTO, User.class);
    }

    public List<User> dtoToEntity(List<UserDTO> dto) {
        return dto.stream().map(this::dtoToEntity).collect(Collectors.toList());
    }
}
