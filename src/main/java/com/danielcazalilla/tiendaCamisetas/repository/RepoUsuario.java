package com.danielcazalilla.tiendaCamisetas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.danielcazalilla.tiendaCamisetas.model.Usuario;
import java.util.List;


@Repository
public interface RepoUsuario extends JpaRepository<Usuario, Long> {

    List<Usuario> findByUsername(String username);
}