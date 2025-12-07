package com.danielcazalilla.tiendaCamisetas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.danielcazalilla.tiendaCamisetas.model.Telefono;
import com.danielcazalilla.tiendaCamisetas.model.Usuario;

@Repository
public interface RepoTelefono extends JpaRepository<Telefono, Long> {
    List<Telefono> findByUsuario(Usuario usuario);
    
} 
