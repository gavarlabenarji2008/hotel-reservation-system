package com.hotel.reservation.controller;

import com.hotel.reservation.model.Customer;
import com.hotel.reservation.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("customers", customerService.findAll());
        return "customers/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customers/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("customer", customerService.findById(id));
        return "customers/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Customer customer, BindingResult result,
                       RedirectAttributes flash) {
        if (result.hasErrors()) {
            return "customers/form";
        }
        try {
            customerService.save(customer);
            flash.addFlashAttribute("success", "Customer saved successfully!");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/customers";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        try {
            customerService.deleteById(id);
            flash.addFlashAttribute("success", "Customer deleted successfully!");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Cannot delete customer: they may have reservations.");
        }
        return "redirect:/customers";
    }
}
