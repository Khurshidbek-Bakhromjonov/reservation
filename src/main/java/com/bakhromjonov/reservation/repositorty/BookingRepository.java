package com.bakhromjonov.reservation.repositorty;

import com.bakhromjonov.reservation.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findByCode(String code);

    void deleteByCode(String code);

}
