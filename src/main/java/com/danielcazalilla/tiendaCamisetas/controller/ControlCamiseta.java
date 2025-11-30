package com.danielcazalilla.tiendaCamisetas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.danielcazalilla.tiendaCamisetas.model.Camiseta;
import com.danielcazalilla.tiendaCamisetas.model.Categoria;
import com.danielcazalilla.tiendaCamisetas.repository.RepoCamiseta;
import com.danielcazalilla.tiendaCamisetas.repository.RepoCategoria;

@Controller
@RequestMapping("/admin")

public class ControlCamiseta {

    @Autowired
    RepoCategoria repoCategoria;
    @Autowired
    RepoCamiseta repoCamiseta;

    @GetMapping("camiseta")
    public String findAll(Model model) {
        model.addAttribute("camisetas", repoCamiseta.findAll());
        return "admin/camisetas";
    }

    @GetMapping("camiseta/categoria/{id}")
    public String findByCategoria(
            Model model,
            @PathVariable(name = "id") Long id) {

        Optional<Categoria> oCategoria = repoCategoria.findById(id);

        if (oCategoria.isPresent()) {
            Categoria padre = oCategoria.get();
            List<Categoria> lCategorias = repoCategoria.findAll();
            model.addAttribute("camisetas", repoCamiseta.findByCategoria(padre));
            model.addAttribute("categorias", lCategorias);
            model.addAttribute("categoria", padre);
            return "admin/camisetas-cat";
        } else {
            model.addAttribute("titulo", "Producto: ERROR");
            model.addAttribute("mensaje", "No puedo encontrar esa categoría en la base de datos");
            return "error";
        }

    }

    @GetMapping("camiseta/categoria")
    public String findByCategorias(Model model) {

        List<Categoria> lCategorias = repoCategoria.findAll();
        model.addAttribute("camisetas", repoCamiseta.findAll());
        model.addAttribute("categorias", lCategorias);

        return "admin/camisetas-cat";
    }

    @GetMapping("camiseta/add")
    public String addForm(Model modelo) {
        modelo.addAttribute("camisetas", repoCamiseta.findAll());
        modelo.addAttribute("camiseta", new Camiseta());
        modelo.addAttribute("categorias", repoCategoria.findAll());
        return "admin/camisetas-add";
    }

    @PostMapping("camiseta/add")
    public String postMethodName(
            @ModelAttribute("camiseta") Camiseta camiseta) {
        repoCamiseta.save(camiseta);
        return "redirect:/admin/camiseta";
    }

    @GetMapping("camiseta/delete/{id}")
    public String deleteForm(
            @PathVariable(name = "id") @NonNull Long id,
            Model modelo) {
        try {
            Optional<Camiseta> camiseta = repoCamiseta.findById(id);
            if (camiseta.isPresent()) {
                // si existe la camiseta
                modelo.addAttribute(
                        "camiseta", camiseta.get());
                return "admin/camiseta-del";
            } else {
                return "error";
            }
        } catch (Exception e) {
            return "error";

        }
    }

    @PostMapping("camiseta/delete/{id}")
    public String delete(
            @PathVariable("id") @NonNull Long id) {
        try {
            repoCamiseta.deleteById(id);
        } catch (Exception e) {
            return "error";
        }

        return "redirect:/admin/camiseta";
    }

    @GetMapping("camiseta/edit/{id}")
    public String editForm(
            @PathVariable @NonNull Long id,
            Model modelo) {

        Optional<Camiseta> camiseta = repoCamiseta.findById(id);
        List<Camiseta> camisetas = repoCamiseta.findAll();

        if (camiseta.isPresent()) {
            modelo.addAttribute("camiseta", camiseta.get());
            modelo.addAttribute("camisetas", camisetas);
            return "admin/camisetas-edit";
        } else {
            modelo.addAttribute(
                    "mensaje",
                    "Camiseta no encontrada");
            modelo.addAttribute(
                    "titulo",
                    "Error en camisetas.");
            return "error";
        }
    }
}
