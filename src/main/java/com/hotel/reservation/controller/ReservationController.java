package com.hotel.reservation.controller;

import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.model.ReservationStatus;
import com.hotel.reservation.service.CustomerService;
import com.hotel.reservation.service.ReservationService;
import com.hotel.reservation.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final CustomerService customerService;
    private final RoomService roomService;

    public ReservationController(ReservationService reservationService,
                                 CustomerService customerService,
                                 RoomService roomService) {
        this.reservationService = reservationService;
        this.customerService = customerService;
        this.roomService = roomService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String status, Model model) {
        if (status != null && !status.isEmpty()) {
            try {
                ReservationStatus rs = ReservationStatus.valueOf(status);
                model.addAttribute("reservations",
                        reservationService.findAll().stream()
                                .filter(r -> r.getStatus() == rs).toList());
            } catch (IllegalArgumentException e) {
                model.addAttribute("reservations", reservationService.findAll());
            }
        } else {
            model.addAttribute("reservations", reservationService.findAll());
        }
        model.addAttribute("statuses", ReservationStatus.values());
        model.addAttribute("selectedStatus", status);
        return "reservations/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("rooms", roomService.findAll());
        return "reservations/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Reservation reservation, BindingResult result,
                       Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("customers", customerService.findAll());
            model.addAttribute("rooms", roomService.findAll());
            return "reservations/form";
        }
        try {
            reservationService.save(reservation);
            flash.addFlashAttribute("success", "Reservation created successfully!");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/reservations/new";
        }
        return "redirect:/reservations";
    }

    @GetMapping("/checkin/{id}")
    public String checkIn(@PathVariable Long id, RedirectAttributes flash) {
        try {
            reservationService.checkIn(id);
            flash.addFlashAttribute("success", "Guest checked in successfully!");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reservations";
    }

    @GetMapping("/checkout/{id}")
    public String checkOut(@PathVariable Long id, RedirectAttributes flash) {
        try {
            reservationService.checkOut(id);
            flash.addFlashAttribute("success", "Guest checked out successfully!");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reservations";
    }

    @GetMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id, RedirectAttributes flash) {
        try {
            reservationService.cancel(id);
            flash.addFlashAttribute("success", "Reservation cancelled.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reservations";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        try {
            reservationService.deleteById(id);
            flash.addFlashAttribute("success", "Reservation deleted.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reservations";
    }
}
