package com.danielcazalilla.tiendaCamisetas.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

//Esta clase sirve para dar en todas las pagina lo del logueo de usuario.
@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("currentUser")
    public String currentUser(Principal principal) {
        return principal != null ? principal.getName() : null;
    }
}
