package com.hotel.reservation.config;

import com.hotel.reservation.model.*;
import com.hotel.reservation.repository.CustomerRepository;
import com.hotel.reservation.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoomRepository roomRepository;
    private final CustomerRepository customerRepository;

    public DataInitializer(RoomRepository roomRepository, CustomerRepository customerRepository) {
        this.roomRepository = roomRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {
        // Only seed if database is empty
        if (roomRepository.count() == 0) {
            roomRepository.save(createRoom("101", RoomType.SINGLE, 75.0, 1, "Cozy single room with garden view"));
            roomRepository.save(createRoom("102", RoomType.DOUBLE, 120.0, 2, "Spacious double room with king bed"));
            roomRepository.save(createRoom("201", RoomType.DELUXE, 200.0, 3, "Deluxe room with ocean view and balcony"));
            roomRepository.save(createRoom("301", RoomType.SUITE, 350.0, 4, "Luxury presidential suite with Jacuzzi"));
            System.out.println("✅ Sample rooms seeded.");
        }

        if (customerRepository.count() == 0) {
            customerRepository.save(createCustomer("John Doe", "john@example.com", "555-0199", "PASSPORT-12345", "123 Elm St, New York"));
            customerRepository.save(createCustomer("Jane Smith", "jane@example.com", "555-0188", "DL-987654", "456 Oak Ave, Los Angeles"));
            System.out.println("✅ Sample customers seeded.");
        }
    }

    private Room createRoom(String number, RoomType type, double price, int capacity, String desc) {
        Room room = new Room();
        room.setRoomNumber(number);
        room.setRoomType(type);
        room.setPricePerNight(price);
        room.setCapacity(capacity);
        room.setStatus(RoomStatus.AVAILABLE);
        room.setDescription(desc);
        return room;
    }

    private Customer createCustomer(String name, String email, String phone, String idProof, String address) {
        Customer c = new Customer();
        c.setName(name);
        c.setEmail(email);
        c.setPhone(phone);
        c.setIdProof(idProof);
        c.setAddress(address);
        return c;
    }
}
