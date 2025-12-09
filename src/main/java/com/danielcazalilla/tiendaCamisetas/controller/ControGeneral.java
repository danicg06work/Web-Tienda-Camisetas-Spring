package com.danielcazalilla.tiendaCamisetas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;



@Controller
public class ControGeneral {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @ModelAttribute("currentUser")
    public String currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        String name = auth.getName();
        if (name == null || "anonymousUser".equals(name)) return null;
        return name;
    }
    
    @GetMapping("/ayuda")
    public String ayuda() {
        return "ayuda";
    }

    @GetMapping("/error")
    public String showError(Model model) {

        model.addAttribute("titulo", "ERROR");
        model.addAttribute("mensaje", "Error genérico");

        return "error";
    }

    @GetMapping("/acerca")
    public String showAcerca() {
        return "acerca";
    }

    
}
