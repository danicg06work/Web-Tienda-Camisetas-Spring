package com.danielcazalilla.tiendaCamisetas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.danielcazalilla.tiendaCamisetas.model.CodigoPostal;
import java.util.List;

@Repository
public interface RepoCodigoPostal extends JpaRepository <CodigoPostal, Long> {
    List<CodigoPostal> findByCodigoPostal(Integer codigoPostal);
    
}
