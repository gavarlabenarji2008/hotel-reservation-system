package com.hotel.reservation.service;

import com.hotel.reservation.model.Room;
import com.hotel.reservation.model.RoomStatus;
import com.hotel.reservation.repository.RoomRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
    }

    public void save(Room room) {
        // Check for duplicate room number (excluding self on edit)
        roomRepository.findByRoomNumber(room.getRoomNumber()).ifPresent(existing -> {
            if (!existing.getId().equals(room.getId())) {
                throw new RuntimeException("Room number " + room.getRoomNumber() + " already exists!");
            }
        });
        roomRepository.save(room);
    }

    public void deleteById(Long id) {
        roomRepository.deleteById(id);
    }

    public List<Room> findAvailableRooms() {
        return roomRepository.findByStatus(RoomStatus.AVAILABLE);
    }

    public long countByStatus(RoomStatus status) {
        return roomRepository.findByStatus(status).size();
    }

    public long count() {
        return roomRepository.count();
    }
}
