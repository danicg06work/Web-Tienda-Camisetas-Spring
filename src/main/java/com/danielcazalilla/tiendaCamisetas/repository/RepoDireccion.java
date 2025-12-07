package com.danielcazalilla.tiendaCamisetas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.danielcazalilla.tiendaCamisetas.model.Direccion;
import com.danielcazalilla.tiendaCamisetas.model.Usuario;

@Repository
public interface RepoDireccion extends JpaRepository<Direccion, Long> {

    List<Direccion> findByUsuario(Usuario usuario);
}
