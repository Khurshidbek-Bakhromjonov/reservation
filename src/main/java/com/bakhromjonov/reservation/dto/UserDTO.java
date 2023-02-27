package com.bakhromjonov.reservation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;

    @Email
    @NotBlank(message = "Please enter the email")
    private String email;

    @Length(max = 30, min = 6)
    @NotBlank(message = "Please enter the password")
    private String password;

    @JsonIgnore
    private Collection<RoleDTO> roles = new ArrayList<>();

    @JsonIgnore
    private Collection<BookingDTO> bookings;
}
