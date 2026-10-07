package com.hotel.reservation.service;

import com.hotel.reservation.model.*;
import com.hotel.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomService roomService;

    public ReservationService(ReservationRepository reservationRepository, RoomService roomService) {
        this.reservationRepository = reservationRepository;
        this.roomService = roomService;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findByOrderByIdDesc();
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
    }

    public void save(Reservation reservation) {
        // Validate dates
        if (!reservation.getCheckOutDate().isAfter(reservation.getCheckInDate())) {
            throw new RuntimeException("Check-out date must be after check-in date!");
        }

        // Resolve the full room entity from the selected room id
        Room selectedRoom = roomService.findById(reservation.getRoom().getId());
        reservation.setRoom(selectedRoom);

        // Check for overlapping bookings
        Long excludeId = reservation.getId();
        List<Reservation> overlaps = reservationRepository.findOverlappingReservations(
                selectedRoom,
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()
        );
        boolean hasConflict = overlaps.stream()
                .anyMatch(r -> excludeId == null || !r.getId().equals(excludeId));

        if (hasConflict) {
            throw new RuntimeException("Room " + selectedRoom.getRoomNumber()
                    + " is already booked for the selected dates!");
        }

        // Calculate total amount
        long nights = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
        reservation.setTotalAmount(nights * selectedRoom.getPricePerNight());

        reservationRepository.save(reservation);
    }

    public void checkIn(Long id) {
        Reservation reservation = findById(id);
        reservation.setStatus(ReservationStatus.CHECKED_IN);
        // Mark room as occupied
        Room room = reservation.getRoom();
        room.setStatus(RoomStatus.OCCUPIED);
        roomService.save(room);
        reservationRepository.save(reservation);
    }

    public void checkOut(Long id) {
        Reservation reservation = findById(id);
        reservation.setStatus(ReservationStatus.CHECKED_OUT);
        // Free up the room
        Room room = reservation.getRoom();
        room.setStatus(RoomStatus.AVAILABLE);
        roomService.save(room);
        reservationRepository.save(reservation);
    }

    public void cancel(Long id) {
        Reservation reservation = findById(id);
        reservation.setStatus(ReservationStatus.CANCELLED);
        // Free room if it was occupied
        Room room = reservation.getRoom();
        if (room.getStatus() == RoomStatus.OCCUPIED) {
            room.setStatus(RoomStatus.AVAILABLE);
            roomService.save(room);
        }
        reservationRepository.save(reservation);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public long count() {
        return reservationRepository.count();
    }

    public long countActive() {
        return reservationRepository.countByStatus(ReservationStatus.CONFIRMED)
             + reservationRepository.countByStatus(ReservationStatus.CHECKED_IN);
    }

    public double getTotalRevenue() {
        return reservationRepository.findAll().stream()
                .filter(r -> r.getStatus() != ReservationStatus.CANCELLED)
                .mapToDouble(Reservation::getTotalAmount)
                .sum();
    }
}
