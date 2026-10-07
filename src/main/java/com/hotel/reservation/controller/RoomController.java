package com.hotel.reservation.controller;

import com.hotel.reservation.model.Room;
import com.hotel.reservation.model.RoomStatus;
import com.hotel.reservation.model.RoomType;
import com.hotel.reservation.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("rooms", roomService.findAll());
        return "rooms/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("roomTypes", RoomType.values());
        model.addAttribute("roomStatuses", RoomStatus.values());
        return "rooms/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("room", roomService.findById(id));
        model.addAttribute("roomTypes", RoomType.values());
        model.addAttribute("roomStatuses", RoomStatus.values());
        return "rooms/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Room room, BindingResult result,
                       Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("roomTypes", RoomType.values());
            model.addAttribute("roomStatuses", RoomStatus.values());
            return "rooms/form";
        }
        try {
            roomService.save(room);
            flash.addFlashAttribute("success", "Room saved successfully!");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/rooms";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        try {
            roomService.deleteById(id);
            flash.addFlashAttribute("success", "Room deleted successfully!");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Cannot delete room: it may have reservations.");
        }
        return "redirect:/rooms";
    }
}
