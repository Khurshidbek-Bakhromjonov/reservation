package com.bakhromjonov.reservation.service;

import com.bakhromjonov.reservation.dto.UserDTO;
import com.bakhromjonov.reservation.mapper.BookingConvert;
import com.bakhromjonov.reservation.mapper.UserConvert;
import com.bakhromjonov.reservation.repositorty.BookingRepository;
import com.bakhromjonov.reservation.repositorty.RoleRepository;
import com.bakhromjonov.reservation.repositorty.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final RoleRepository roleRepository;
    private final UserConvert userConvert;
    private final BookingConvert bookingConvert;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailSenderService emailSenderService;


    public List<UserDTO>



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
