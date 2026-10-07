package com.hotel.reservation.controller;

import com.hotel.reservation.model.RoomStatus;
import com.hotel.reservation.service.CustomerService;
import com.hotel.reservation.service.ReservationService;
import com.hotel.reservation.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final RoomService roomService;
    private final CustomerService customerService;
    private final ReservationService reservationService;

    public DashboardController(RoomService roomService, CustomerService customerService,
                               ReservationService reservationService) {
        this.roomService = roomService;
        this.customerService = customerService;
        this.reservationService = reservationService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalRooms", roomService.count());
        model.addAttribute("availableRooms", roomService.countByStatus(RoomStatus.AVAILABLE));
        model.addAttribute("occupiedRooms", roomService.countByStatus(RoomStatus.OCCUPIED));
        model.addAttribute("totalCustomers", customerService.count());
        model.addAttribute("totalReservations", reservationService.count());
        model.addAttribute("activeReservations", reservationService.countActive());
        model.addAttribute("totalRevenue", reservationService.getTotalRevenue());
        model.addAttribute("recentReservations", reservationService.findAll().stream().limit(5).toList());
        return "dashboard";
    }
}
