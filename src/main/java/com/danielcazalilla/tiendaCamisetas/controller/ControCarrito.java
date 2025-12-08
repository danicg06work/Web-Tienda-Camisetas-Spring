package com.danielcazalilla.tiendaCamisetas.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.lang.NonNull;

import com.danielcazalilla.tiendaCamisetas.exception.CarroException;
import com.danielcazalilla.tiendaCamisetas.model.Camiseta;
import com.danielcazalilla.tiendaCamisetas.model.Estado;
import com.danielcazalilla.tiendaCamisetas.model.LineaPedido;
import com.danielcazalilla.tiendaCamisetas.model.Pedido;
import com.danielcazalilla.tiendaCamisetas.model.Usuario;
import com.danielcazalilla.tiendaCamisetas.repository.RepoCamiseta;
import com.danielcazalilla.tiendaCamisetas.repository.RepoLineaPedido;
import com.danielcazalilla.tiendaCamisetas.repository.RepoPedido;
import com.danielcazalilla.tiendaCamisetas.repository.RepoUsuario;
//import com.danielcazalilla.tiendaCamisetas.repository.RepoTelefono;
//import com.danielcazalilla.tiendaCamisetas.repository.RepoDireccion;
import jakarta.transaction.Transactional;

@Controller
public class ControCarrito {
    
    @Autowired
    private RepoCamiseta repoCamiseta;

    @Autowired
    private RepoPedido repoPedido;

    @Autowired
    private RepoUsuario repoUsuario;

    @Autowired 
    private RepoLineaPedido repoLineaPedido;

    /* 
    @Autowired
    private RepoDireccion repoDireccion;

    @Autowired
    private RepoTelefono repoTelefono;
    */
    
    private Usuario getLoggedUser() {
        // Del contexto de la aplicación obtenemos el usuario
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        // obtenemos el usuario del repositorio por su "username"
        return repoUsuario.findByUsername(username).get(0);
    }

   
    @GetMapping({"/camisetas","/productos"})
    public String findAll(Model modelo) {
        List<Camiseta> camisetas = repoCamiseta.findAll();
        camisetas.removeIf(camiseta -> camiseta.getStock()==0);
        modelo.addAttribute(
            "camisetas", 
            camisetas);
        return "carro/productos";
    }
    
    
    @GetMapping("/carro")
    public String findCarro(Model modelo) {

        List<LineaPedido> lineaPedidos = null;

        Usuario cliente = getLoggedUser();

        long total = 0;
        
        // Para el usuario que hizo login, buscamos un pedido (sólo puede haber uno) en estado "CARRITO"
        List<Pedido> pedidos = repoPedido.findByEstadoAndCliente(Estado.CARRITO, cliente);
        if (pedidos.size()>0) {
            lineaPedidos = repoLineaPedido.findByPedido(pedidos.get(0));
            for (LineaPedido lp : pedidos.get(0).getLineaPedidos()) {
                total += lp.getCantidad()*lp.getCamiseta().getPrecio();
            }
        }
        

        // mandamos a la vista los modelos: Pedido y su lista de LineaPedido             
        modelo.addAttribute("lineapedidos", lineaPedidos);
        modelo.addAttribute("total", total);

        // modelo.addAttribute("productos", productos );
        return "carro/carro";
    }
    


    @GetMapping("/carro/add/{id}")
    public String addForm(
        @PathVariable @NonNull Long id, Model modelo) {

        Optional <Camiseta> camiseta = repoCamiseta.findById(id);        

        if (camiseta.isPresent()){ 
            // Si el producto está ya en el carro, haremos un "edit"
            List<Pedido> pedidos = repoPedido.findByEstadoAndCliente(Estado.CARRITO, getLoggedUser());
            if (pedidos.size()>0) {
                Pedido carro = pedidos.get(0);
                for (LineaPedido lp : carro.getLineaPedidos()) {
                    if(lp.getCamiseta().getId()==id) {
                        modelo.addAttribute("lineaPedido", lp);
                        modelo.addAttribute("camiseta", lp.getCamiseta());
                        modelo.addAttribute("cantidad", lp.getCantidad());
                        return "carro/carro-edit";                        
                    }
                }
            }
            modelo.addAttribute("camiseta", camiseta.get());            
        } else {
            modelo.addAttribute("titulo", "Error al añadir al carrito");
            modelo.addAttribute("mensaje", "No se ha podido encontrar ese producto en la base de datos");
            return "error";
        } 
        return "carro/carro-add";
    }


     
    @PostMapping("/carro/add/{id}")
    public String add(
        @PathVariable @NonNull Long id, 
        @RequestParam @NonNull Integer cantidad,
        Model modelo) {

        String vista = "redirect:/carro";
        Optional <Camiseta> camiseta = repoCamiseta.findById(id);        
        Usuario cliente = getLoggedUser();
        Pedido carrito ;

        List<Pedido> pedidos = repoPedido.findByEstadoAndCliente(Estado.CARRITO, cliente);

        // ahora añadimos una línea al pedido        
        if (camiseta.isPresent() && cantidad>0 ){ 
            // si no existe carro de la compra se crea
            if (pedidos.size()>0) {
                carrito = pedidos.get(0);
            } else {
                carrito = new Pedido();
                carrito.setCliente(cliente);
                carrito.setEstado(Estado.CARRITO);
                carrito = repoPedido.save(carrito);
            }
            LineaPedido lineaPedido = new LineaPedido();

            if (carrito.getLineaPedidos()!= null) {
                // TEST para ver si ya estaba en el carro el producto
                for (LineaPedido lp : carrito.getLineaPedidos()) {
                    if(lp.getCamiseta().getId()==id) {
                        cantidad = lp.getCantidad()+cantidad;
                        lineaPedido = lp;
                    }
                }
            }

            // TEST para ver si queda stock
            if (cantidad <= camiseta.get().getStock()) {                
                lineaPedido.setCamiseta(camiseta.get());
                lineaPedido.setCantidad(cantidad);
                lineaPedido.setPedido(carrito);
                lineaPedido = repoLineaPedido.save(lineaPedido);
            } else {
                modelo.addAttribute("titulo", "Error al añadir " + camiseta.get().getNombre() + " al carrito");
                modelo.addAttribute("mensaje", "No hay suficiente stock (quedan " + camiseta.get().getStock() + " unidades).");  
                vista = "error";
            }
        } else {
            modelo.addAttribute("titulo", "Error al añadir al carrito");
            modelo.addAttribute("mensaje", "No se ha podido encontrar ese producto en la base de datos");
            vista = "error";
        }

        return vista;
    }

    
    @GetMapping("/carro/edit/{id}")
    public String editForm(
         @PathVariable @NonNull Long id, Model modelo) {
        
        String vista;
        
        Optional <LineaPedido> lp = repoLineaPedido.findById(id);
        if (lp.isPresent()){ 
            // comprobamos que el pedido pertenece al usuario que hizo login
            // sin esta comprobación, ¡¡podríamos ver productos en carros de otros usuarios!!
            List<LineaPedido> lineaPedidos = 
                repoLineaPedido.lineaPedidoBelongsToUser(lp.get(), getLoggedUser());
            if (lineaPedidos.size()>0){
                modelo.addAttribute("lineaPedido", lineaPedidos.get(0));
                modelo.addAttribute("camiseta", lp.get().getCamiseta());
                modelo.addAttribute("cantidad", lp.get().getCantidad());
                vista = "carro/carro-edit";
            } else {
                modelo.addAttribute("titulo", "Error al editar camisetas del carrito");
                modelo.addAttribute("mensaje", "No se ha podido encontrar esa camiseta en su carro");
                vista = "error";
            }
        } else {
            modelo.addAttribute("titulo", "Error al añadir al carrito");
            modelo.addAttribute("mensaje", "No se ha podido encontrar esa camiseta en la base de datos");
            vista = "error";
        }
        
        return vista;
    }

    @PostMapping("/carro/edit")
    public String edit(
        @ModelAttribute("lineaPedido") @NonNull LineaPedido lineaPedido, 
        Model modelo) {
        
        String vista = "redirect:/carro";
    
        // Aunque nos llega el objeto por si nos manipulan el mismo en el formulario, lo buscamos por ID
        Optional <LineaPedido> lp = repoLineaPedido.findById(lineaPedido.getId());        

        if (lp.isPresent()){ 
            LineaPedido oldLineaPedido = lp.get();
            // comprobamos que el pedido pertenece al usuario que hizo login
            // sin esta comprobación, ¡¡podríamos ver productos en carros de otros usuarios!!
            List<LineaPedido> lineaPedidos = repoLineaPedido.lineaPedidoBelongsToUser(lineaPedido, getLoggedUser());
            if (lineaPedidos.size()>0 && oldLineaPedido.getCamiseta().getStock()>=lineaPedido.getCantidad()){
                oldLineaPedido.setCantidad(lineaPedido.getCantidad());
                repoLineaPedido.save(oldLineaPedido);
            } else {
                modelo.addAttribute("titulo", "Error al editar camisetas del carrito");
                modelo.addAttribute("mensaje", "Sin stock para poder hacer el cambio");
                vista = "error";
            }
        } else {
            modelo.addAttribute("titulo", "Error al añadir al carrito");
            modelo.addAttribute("mensaje", "No se ha podido encontrar ese producto en la base de datos");
            vista = "error";
        }
        
        return vista;
        
    }
    
    
    @GetMapping("/carro/del/{id}")
    public String delForm(
        @PathVariable @NonNull Long id, Model modelo) {
        
        String vista;
        
        Optional <LineaPedido> lp = repoLineaPedido.findById(id);
        if (lp.isPresent()){ 
            // comprobamos que el pedido pertenece al usuario que hizo login
            // sin esta comprobación, ¡¡podríamos ver productos en carros de otros usuarios!!
            List<LineaPedido> lineaPedidos = repoLineaPedido.lineaPedidoBelongsToUser(lp.get(), getLoggedUser());
            if (lineaPedidos.size()>0){
                modelo.addAttribute("lineaPedido", lineaPedidos.get(0));                
                vista = "carro/carro-del";
            } else {
                modelo.addAttribute("titulo", "Error al borrar camisetas del carrito");
                modelo.addAttribute("mensaje", "No se ha podido encontrar esa camiseta en su carro");
                vista = "error";
            }
        } else {
            modelo.addAttribute("titulo", "Error al borrar del carrito");
            modelo.addAttribute("mensaje", "No se ha podido encontrar esa camiseta en la base de datos");
            vista = "error";
        }
        
        return vista;
    }

  
    @PostMapping("/carro/del")
    public String delete(
        @RequestParam @NonNull Long id, Model modelo) {
        
        String vista;
        
        Optional <LineaPedido> lp = repoLineaPedido.findById(id);
        if (lp.isPresent()){ 
            // comprobamos que el pedido pertenece al usuario que hizo login
            // sin esta comprobación, ¡¡podríamos ver productos en carros de otros usuarios!!
            List<LineaPedido> lineaPedidos = repoLineaPedido.lineaPedidoBelongsToUser(lp.get(), getLoggedUser());
            if (lineaPedidos.size()>0){
                Pedido carro = lineaPedidos.get(0).getPedido();
                repoLineaPedido.delete(lineaPedidos.get(0));
                // si es el último producto del carro borramos el carro
                if (carro.getLineaPedidos().size()==0)
                    repoPedido.delete(carro);
                
                vista = "redirect:/carro";
            } else {
                modelo.addAttribute("titulo", "Error al borrar camisetas del carrito");
                modelo.addAttribute("mensaje", "No se ha podido encontrar esa camiseta en su carro");
                vista = "error";
            }
        } else {
            modelo.addAttribute("titulo", "Error al borrar del carrito");
            modelo.addAttribute("mensaje", "No se ha podido encontrar esa camiseta en la base de datos");
            vista = "error";
        }
        
        return vista;
    }


    @GetMapping("/carro/confirmar")
    public String confirmForm(Model modelo) {

        Usuario loggedUser = getLoggedUser();
        long total = 0;

        // Para el usuario que hizo login, buscamos el pedido (sólo puede haber uno) en estado "CARRITO"
        List<Pedido> pedidos = repoPedido.findByEstadoAndCliente(Estado.CARRITO, loggedUser);
        if (pedidos.size()==1) {
            modelo.addAttribute("pedido", pedidos.get(0));            
            modelo.addAttribute("direcciones", loggedUser.getDirecciones());
            modelo.addAttribute("telefonos", loggedUser.getTelefonos());

            for (LineaPedido lp : pedidos.get(0).getLineaPedidos()) {
                total += lp.getCantidad()*lp.getCamiseta().getPrecio();
            }
            modelo.addAttribute("total", total);
            
            return "carro/carro-confirm";   
        } else {
            modelo.addAttribute("titulo", "Error al confirmar el pedido");
            modelo.addAttribute("mensaje", "No se ha podido encontrar ese pedido en la base de datos");
            return "error";
        }
    }
    
    @PostMapping("/carro/confirmar")
    @Transactional(rollbackOn = CarroException.class)
    public String confirm(
        @ModelAttribute("lineaPedido") @NonNull Pedido pedido,
        Model modelo) throws CarroException {

        Usuario loggedUser = getLoggedUser();
        long total = 0;            

        // Para el usuario que hizo login, buscamos el pedido (sólo puede haber uno) en estado "CARRITO"
        List<Pedido> pedidos = repoPedido.findByEstadoAndCliente(Estado.CARRITO, loggedUser);
        if (pedidos.size()==1 ) {
            if(pedidos.get(0).getId()==pedido.getId()) {
                pedido.setCliente(loggedUser);
                pedido.setDescuento(Float.valueOf(0));
                pedido.setEstado(Estado.REALIZADO);
                pedido.setFecha(LocalDate.now());                
                for (LineaPedido lp : pedidos.get(0).getLineaPedidos()) {
                    // comprobamos si hay stock
                    Camiseta p = lp.getCamiseta();
                    if (p.getStock()>=lp.getCantidad()) {
                        lp.setPrecio(lp.getCamiseta().getPrecio());
                        total += lp.getCantidad()*lp.getCamiseta().getPrecio();
                        p.setStock(p.getStock()-lp.getCantidad());
                        repoCamiseta.save(p);
                    } else {
                        throw new CarroException(
                            "No queda suficiente stock de: " + 
                            p.getNombre() + 
                            " para completar el pedido. Sólo quedan: " + 
                            p.getStock()+
                            " unidades y en el pedido se solicitan: " + 
                            lp.getCantidad() + 
                            ". Intente poner menos unidades para completar el pedido.");
                    }
                }
                pedido.setTotal(Float.valueOf(total));
                repoPedido.save(pedido);
            } else {
                modelo.addAttribute("titulo", "Error al confirmar el pedido");
                modelo.addAttribute("mensaje", "Los datos del pedido no son válidos.");
                return "error";
            }
        } else {
            modelo.addAttribute("titulo", "Error al confirmar el pedido");
            modelo.addAttribute("mensaje", "No se ha podido encontrar ese pedido en la base de datos");
            return "error";
        }

        return "redirect:/mis-pedidos";
    }

    @GetMapping("/mis-pedidos")
    public String getPedidos(Model modelo) {
        List<Pedido> pedidos = repoPedido.findByCliente(getLoggedUser());
        // quitamos el carro de la compra de la lista
        pedidos.removeIf( x -> x.getEstado() == Estado.CARRITO);
        modelo.addAttribute("pedidos", pedidos);
        if (pedidos.size()>0)
            return "pedido/pedidos";
        else {
            modelo.addAttribute("titulo", "Error al mostrar sus pedidos");
            modelo.addAttribute("mensaje", "No se ha podido encontrar ningún pedido en la base de datos");            
            return "error";
        }
    }
    
    @GetMapping("/mis-pedidos/{id}")
    public String detallePedido(
        @PathVariable @NonNull Long id,
        Model modelo) {
        String vista = "pedido/detalle";
        Optional<Pedido> oPedido = repoPedido.findById(id);
        if (oPedido.isPresent()){
            modelo.addAttribute("pedido", oPedido.get());
            modelo.addAttribute("lineaPedidos", oPedido.get().getLineaPedidos());
        }
        else { 
            modelo.addAttribute("titulo", "Error al mostrar pedidos");
            modelo.addAttribute("mensaje", "No se ha podido encontrar ese pedido en la base de datos");
            vista = "error";
        }
        return vista;
    }   

}