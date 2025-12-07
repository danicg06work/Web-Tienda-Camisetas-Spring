package com.danielcazalilla.tiendaCamisetas.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.danielcazalilla.tiendaCamisetas.model.Direccion;
import com.danielcazalilla.tiendaCamisetas.model.Telefono;
import com.danielcazalilla.tiendaCamisetas.model.Usuario;
import com.danielcazalilla.tiendaCamisetas.repository.RepoDireccion;
import com.danielcazalilla.tiendaCamisetas.repository.RepoTelefono;
import com.danielcazalilla.tiendaCamisetas.repository.RepoUsuario;



@Controller
@RequestMapping("/mis-datos")
public class ControUsuario {
        
    @Autowired
    private RepoUsuario repoUsuario;

    @Autowired
    private RepoTelefono repoTelefono;

    @Autowired
    private RepoDireccion repoDireccion;

    
    private Usuario getLoggedUser() {
        // Del contexto de la aplicación obtenemos el usuario
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        // obtenemos el usuario del repositorio por su "username"
        Usuario cliente = repoUsuario.findByUsername(username).get(0);

        return cliente;
    }

    
    @GetMapping("")
    public String getUsuarioForm(Model modelo) {
        return getUsuario(modelo);
    }

    @GetMapping("/")
    public String getUsuario(Model modelo) {
        modelo.addAttribute("usuario", getLoggedUser());
        return "usuario/usuario";
    }
    
    @PostMapping("")
    public String postUsuario(Model modelo,
    @ModelAttribute Usuario usuario) {
        String vista = "redirect:/mis-datos";
        Usuario givenUser = getLoggedUser();
        if (usuario.getId() == givenUser.getId()) {
            if (usuario.getPassword().length()>0) 
                givenUser.setPassword(
                    new BCryptPasswordEncoder().encode(usuario.getPassword()));
            givenUser.setApellido(usuario.getApellido());
            givenUser.setEmail(usuario.getEmail());
            givenUser.setNombre(usuario.getNombre());
            repoUsuario.save(givenUser);
        } else {
            modelo.addAttribute("titulo", "Error al actualizar sus datos");
            modelo.addAttribute("mensaje", "No se ha podido actualizar su información en la base de datos");
            vista = "error";
        }
        return vista;
    }

    @PostMapping("/")
    public String postUsuarioForm(Model modelo,
        @ModelAttribute Usuario usuario) {
        return postUsuario(modelo, usuario);
    }


    @GetMapping("/telefonos")
    public String getPhonesByUserId(
            Model modelo) {

        Usuario usuario = getLoggedUser();
        
        modelo.addAttribute(
                "usuarioActual", usuario);
        modelo.addAttribute(
                "telefonos", usuario.getTelefonos());

        return "usuario/telefonos/telefonos";
    }

   
    @GetMapping("/telefonos/add")
    public String usuarioAddPhoneForm(
            Model modelo) {

        Usuario usuario = getLoggedUser();

        Telefono telefono = new Telefono();
        telefono.setUsuario(usuario);

        modelo.addAttribute(
            "telefono", telefono);
        modelo.addAttribute(
            "usuario", usuario);

        return "usuario/telefonos/add";
    }

  
    @PostMapping("/telefonos/add")
    public String usuarioAddPhone(
            @ModelAttribute("telefono") @NonNull Telefono telefono,
            Model modelo) {

        telefono.setUsuario(getLoggedUser());
        repoTelefono.save(telefono);

        return "redirect:/mis-datos/telefonos";
    }

    @GetMapping("/telefonos/edit/{idTel}")
    public String editPhonesByUserIdForm(
            @PathVariable @NonNull Long idTel,
            Model modelo) {

        Usuario loggedUser = getLoggedUser();
        Optional<Telefono> oTelefono = repoTelefono.findById(idTel);

        if (!oTelefono.isPresent() ) {
            modelo.addAttribute("titulo", "Error al actualizar sus datos");
            modelo.addAttribute("mensaje", "No se ha podido encontrar el teléfono en la base de datos.");
            return "error";
        } else {
            if (loggedUser.getId()!=oTelefono.get().getUsuario().getId()) {
                modelo.addAttribute("titulo", "Error al actualizar sus datos");
                modelo.addAttribute("mensaje", "Ese teléfono no se ha podido encontrar.");
                return "error";
            } else {
                modelo.addAttribute(
                "usuario", loggedUser);
                modelo.addAttribute(
                        "telefono", oTelefono.get());
                return "usuario/telefonos/edit";
            }
        }        
    }

    @GetMapping("/telefonos/delete/{idTel}")
    public String delPhonesByUserIdForm(
            @PathVariable @NonNull Long idTel,
            Model modelo) {

        Usuario loggedUser = getLoggedUser();
        Optional<Telefono> oTelefono = repoTelefono.findById(idTel);

        if (!oTelefono.isPresent() ) {
            modelo.addAttribute("titulo", "Error al actualizar sus datos");
            modelo.addAttribute("mensaje", "No se ha podido encontrar el teléfono en la base de datos");
            return "error";
        } else {
            if (loggedUser.getId()!=oTelefono.get().getUsuario().getId()) {
                modelo.addAttribute("titulo", "Error al actualizar sus datos");
                modelo.addAttribute("mensaje", "No se ha podido encontrar el teléfono en la base de datos");
                return "error";
            } else {
                modelo.addAttribute(
                "usuario", loggedUser);
                modelo.addAttribute(
                        "telefono", oTelefono.get());
                return "usuario/telefonos/delete";
            }
        }        
    }

    @PostMapping("/telefonos/delete/{idTel}")
    public String delPhonesByUserId(
            @PathVariable @NonNull Long idTel,
            Model modelo) {

        Usuario loggedUser = getLoggedUser();
        Optional<Telefono> oTelefono = repoTelefono.findById(idTel);

        if (!oTelefono.isPresent() ) {
            modelo.addAttribute("titulo", "Error al actualizar sus datos");
            modelo.addAttribute("mensaje", "No se ha podido encontrar el teléfono en la base de datos");
            return "error";
        }

        if (loggedUser.getId() != oTelefono.get().getUsuario().getId()) {
            modelo.addAttribute("titulo", "Error al actualizar sus datos");
            modelo.addAttribute("mensaje", "No se ha podido encontrar su teléfono en la base de datos");            
            return "error";
        }

        
        repoTelefono.delete(oTelefono.get());

        return "redirect:/mis-datos/telefonos";
    }

    @GetMapping("/direcciones")
    public String getAddressByUserId(
            Model modelo) {

        Usuario usuario = getLoggedUser();
        
        modelo.addAttribute(
                "usuarioActual", usuario);
        modelo.addAttribute(
                "direcciones", usuario.getDirecciones());

        return "usuario/direcciones/direcciones";
    }

    @GetMapping("/direcciones/add")
    public String usuarioAddAddressForm(
            Model modelo) {

        Usuario usuario = getLoggedUser();

        Direccion direccion = new Direccion();
        direccion.setUsuario(usuario);

        modelo.addAttribute(
            "direccion", direccion);
        modelo.addAttribute(
            "usuario", usuario);

        return "usuario/direcciones/add";
    }

   
    @PostMapping("/direcciones/add")
    public String usuarioAddPhone(
            @ModelAttribute("direccion") @NonNull Direccion direccion,
            Model modelo) {

        direccion.setUsuario(getLoggedUser());
        repoDireccion.save(direccion);

        return "redirect:/mis-datos/direcciones";
    }

   
    @GetMapping("/direcciones/edit/{idDir}")
    public String usuarioEditAddressForm(
            @PathVariable Long idDir,
            Model modelo) {

        Usuario usuario = getLoggedUser();

        Optional<Direccion> oDir = repoDireccion.findById(idDir);

        if (oDir.isPresent()) {
            Direccion direccion = oDir.get();

            if (direccion.getUsuario().getId()==usuario.getId()) {
                modelo.addAttribute(
                    "direccion", direccion);
                modelo.addAttribute(
                    "usuario", usuario);
    
                return "usuario/direcciones/edit";

            } else {
                modelo.addAttribute(
                    "titulo", "Error al actualizar sus datos");
                modelo.addAttribute(
                    "mensaje", "No se ha podido encontrar su dirección en la base de datos");
                return "error";
            }           
        } else {
            modelo.addAttribute(
            "titulo", "Error al actualizar sus datos");
            modelo.addAttribute(
            "mensaje", "No se ha podido encontrar su teléfono en la base de datos");
            return "error";
        }
    }


   
    @GetMapping("/direcciones/delete/{idDir}")
    public String usuarioDeleteAddressForm(
            @PathVariable Long idDir,
            Model modelo) {

        Usuario usuario = getLoggedUser();

        Optional<Direccion> oDir = repoDireccion.findById(idDir);

        if (oDir.isPresent()) {
            Direccion direccion = oDir.get();

            if (direccion.getUsuario().getId()==usuario.getId()) {
                modelo.addAttribute(
                    "direccion", direccion);
                modelo.addAttribute(
                    "usuario", usuario);
    
                return "usuario/direcciones/delete";

            } else {
                modelo.addAttribute(
                    "titulo", "Error al actualizar sus datos");
                modelo.addAttribute(
                    "mensaje", "No se ha podido encontrar su dirección en la base de datos");
                return "error";
            }           
        } else {
            modelo.addAttribute(
            "titulo", "Error al actualizar sus datos");
            modelo.addAttribute(
            "mensaje", "No se ha podido encontrar su teléfono en la base de datos");
            return "error";
        }
    }


   
    @PostMapping("/direcciones/delete/{idDir}")
    public String usuarioDeleteAddress(
            @PathVariable Long idDir,
            Model modelo) {

        Usuario usuario = getLoggedUser();

        Optional<Direccion> oDir = repoDireccion.findById(idDir);

        if (oDir.isPresent()) {
            Direccion direccion = oDir.get();
            if (direccion.getUsuario().getId()==usuario.getId()) {                
                repoDireccion.delete(direccion);
                return "redirect:/mis-datos/direcciones";
            } else {
                modelo.addAttribute(
                    "titulo", "Error al actualizar sus datos");
                modelo.addAttribute(
                    "mensaje", "No se ha podido encontrar su dirección en la base de datos");
                return "error";
            }           
        } else {
            modelo.addAttribute(
            "titulo", "Error al actualizar sus datos");
            modelo.addAttribute(
            "mensaje", "No se ha podido encontrar su teléfono en la base de datos");
            return "error";
        }
    }
}
