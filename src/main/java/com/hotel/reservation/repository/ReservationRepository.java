package com.hotel.reservation.repository;

import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.model.ReservationStatus;
import com.hotel.reservation.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByStatus(ReservationStatus status);

    List<Reservation> findByOrderByIdDesc();

    // Check if a room is already booked for overlapping dates
    @Query("SELECT r FROM Reservation r WHERE r.room = :room " +
           "AND r.status NOT IN ('CANCELLED', 'CHECKED_OUT') " +
           "AND r.checkInDate < :checkOut " +
           "AND r.checkOutDate > :checkIn")
    List<Reservation> findOverlappingReservations(
        @Param("room") Room room,
        @Param("checkIn") LocalDate checkIn,
        @Param("checkOut") LocalDate checkOut
    );

    long countByStatus(ReservationStatus status);
}
