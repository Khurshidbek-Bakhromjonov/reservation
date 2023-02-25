package com.bakhromjonov.reservation.repositorty;

import com.bakhromjonov.reservation.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Room findByName(String name);

    @Query(value = "SELECT * FROM room AS ro "
            + "WHERE ro.id NOT IN "
            + "("
            + "SELECT re.room_id "
            + "FROM booking AS re "
            + "WHERE (re.start_date BETWEEN ?1 AND ?2) "
            + "OR (re.end_date BETWEEN ?1 AND  ?2) "
            + ")", nativeQuery = true)
    List<Room> findMeetingRoomAvailable(LocalDateTime db, LocalDateTime de);

    @Query(value = "SELECT * FROM (SELECT * FROM room AS ro WHERE ro.id NOT IN "
            + "(SELECT re.room_id FROM booking AS re WHERE (re.start_date BETWEEN ?1 AND  ?2) "
            + "OR (re.end_date BETWEEN ?1 AND ?2))) AS roo WHERE roo.id = ?3", nativeQuery = true)
    Room checkAvailability(LocalDateTime db, LocalDateTime de, Long id);
}
