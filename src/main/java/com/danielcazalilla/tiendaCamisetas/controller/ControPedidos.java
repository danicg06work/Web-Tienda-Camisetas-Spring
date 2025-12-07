package com.danielcazalilla.tiendaCamisetas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.danielcazalilla.tiendaCamisetas.model.Estado;
import com.danielcazalilla.tiendaCamisetas.model.Pedido;
import com.danielcazalilla.tiendaCamisetas.model.Usuario;
import com.danielcazalilla.tiendaCamisetas.repository.RepoPedido;
import com.danielcazalilla.tiendaCamisetas.repository.RepoUsuario;



@Controller
@RequestMapping("/pedidos")
public class ControPedidos {
    
    @Autowired
    private RepoPedido repoPedido;

    @Autowired
    private RepoUsuario repoUsuario;

    @GetMapping("")
    public String getPedidosRealizados(
        Model modelo) {
        
        modelo.addAttribute(
            "currentUrl", "/pedidos/preparar");
        modelo.addAttribute(
            "pedidos", repoPedido.findByEstado(Estado.REALIZADO));

        
        return "/pedidos/pedidos";
    }

    private Usuario getLoggedUser() {
        // Del contexto de la aplicación obtenemos el usuario
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        // obtenemos el usuario del repositorio por su "username"
        Usuario cliente = repoUsuario.findByUsername(username).get(0);

        return cliente;
    }
    
    @GetMapping("/preparar/{id}")
    public String getDetallePedido(
        @PathVariable Long id,
        Model modelo) {
                
        Optional<Pedido> oPedido = repoPedido.findById(id);

        modelo.addAttribute("pedido", oPedido.get());
        
        return "/pedidos/servir";
    }

    @PostMapping("/preparar/{id}")
    public String setPedidoAsPreparando(
        @PathVariable Long id,
        Model modelo) {
                
        Optional<Pedido> oPedido = repoPedido.findById(id);

        Pedido pedido = oPedido.get();
        pedido.setEstado(Estado.PREPARANDO);
        pedido.setOperario(getLoggedUser());

        repoPedido.save(pedido);
        
        return "redirect:/pedidos/en-preparacion";
    }

    @GetMapping("/en-preparacion")
    public String findPedidosEnPreparacion(
        Model modelo){

        Usuario usuario = getLoggedUser();
        List<Pedido> pedidos = repoPedido.findByEstadoAndOperario(
            Estado.PREPARANDO, usuario);
        
        modelo.addAttribute("currentUrl", "/pedidos/en-preparacion");
        modelo.addAttribute("pedidos", pedidos);

        return "/pedidos/pedidos";
    }
 
    @GetMapping("/en-preparacion/{id}")
    public String prepararPedido(
        @PathVariable Long id,
        Model modelo){

        Optional<Pedido> oPedido = repoPedido.findById(id);

        modelo.addAttribute("pedido", oPedido.get());
        
        return "/pedidos/servir";        
    }

    @PostMapping("/en-preparacion/{id}")
    public String setPedidoAsEnviado(
        @PathVariable Long id,
        Model modelo) {
                
        Optional<Pedido> oPedido = repoPedido.findById(id);

        Pedido pedido = oPedido.get();
        pedido.setEstado(Estado.ENVIADO);
        pedido.setOperario(getLoggedUser());

        repoPedido.save(pedido);
        
        return "redirect:/pedidos/enviados";
    }

    @GetMapping("/enviados")
    public String findPedidosEnviados(
        Model modelo){

        Usuario usuario = getLoggedUser();
        List<Pedido> pedidos = repoPedido.findByEstadoAndOperario(
            Estado.ENVIADO, usuario);
        
        modelo.addAttribute("currentUrl", "/pedidos/en-preparacion");
        modelo.addAttribute("pedidos", pedidos);

        return "/pedidos/pedidos";
    }

    @GetMapping("/mis-pedidos")
    public String findMyPedidos(
        Model modelo) {
        
        Usuario usuario = getLoggedUser();
        List<Pedido> pedidos = repoPedido.findByOperario(usuario);

        modelo.addAttribute("pedidos", pedidos);
        
        return "pedidos/pedidos";
    }
    
}
