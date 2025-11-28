package ru.ilug.business_card_website.infrastructure.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalErrorController {

    @ExceptionHandler(ResponseStatusException.class)
    public String handle(ResponseStatusException ex, Model model) {
        model.addAttribute("code", ex.getStatusCode().value());
        model.addAttribute("reason", ex.getReason());
        return "error";
    }

}
