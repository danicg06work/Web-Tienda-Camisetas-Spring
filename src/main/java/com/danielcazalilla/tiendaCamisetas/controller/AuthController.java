package com.danielcazalilla.tiendaCamisetas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.danielcazalilla.tiendaCamisetas.model.Rol;
import com.danielcazalilla.tiendaCamisetas.model.RolUsuario;
import com.danielcazalilla.tiendaCamisetas.model.Usuario;
import com.danielcazalilla.tiendaCamisetas.repository.RepoRolUsuario;
import com.danielcazalilla.tiendaCamisetas.repository.RepoUsuario;

@Controller
public class AuthController {

    @Autowired
    private RepoUsuario repoUsuario;

    @Autowired
    private RepoRolUsuario repoRolUsuario;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute Usuario usuario, Model model) {

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setEnabled(true);

        repoUsuario.save(usuario);

        RolUsuario rol = new RolUsuario();
        rol.setUsuario(usuario);
        rol.setRol(Rol.CLIENTE);
        repoRolUsuario.save(rol);

        return "redirect:/login?registered";
    }
}
