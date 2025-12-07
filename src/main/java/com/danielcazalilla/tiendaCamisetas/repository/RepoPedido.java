package com.danielcazalilla.tiendaCamisetas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.danielcazalilla.tiendaCamisetas.model.Estado;
import com.danielcazalilla.tiendaCamisetas.model.Pedido;
import com.danielcazalilla.tiendaCamisetas.model.Usuario;


public interface RepoPedido extends JpaRepository <Pedido, Long>{
    
    List<Pedido> findByEstado(Estado estado);

    @Query("SELECT pedido FROM Pedido pedido  WHERE pedido.estado != ?1")
    List<Pedido> findDistinctEstado(Estado estado);
    
    List<Pedido> findByEstadoAndCliente(Estado estado, Usuario cliente);
    List<Pedido> findByCliente(Usuario cliente);
    List<Pedido> findByOperario(Usuario operario);
    List<Pedido> findByEstadoAndOperario(Estado estado, Usuario operario);
    
}