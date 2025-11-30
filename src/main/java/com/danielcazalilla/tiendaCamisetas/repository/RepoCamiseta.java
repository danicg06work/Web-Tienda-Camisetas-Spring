package com.danielcazalilla.tiendaCamisetas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.danielcazalilla.tiendaCamisetas.model.Camiseta;
import com.danielcazalilla.tiendaCamisetas.model.Categoria;

@Repository
public interface RepoCamiseta extends JpaRepository<Camiseta, Long> {
    List<Camiseta> findByCategoria(Categoria categoria);
}