package com.danielcazalilla.tiendaCamisetas.controller;

import org.springframework.web.bind.annotation.RestController;

import com.danielcazalilla.tiendaCamisetas.model.CodigoPostal;
import com.danielcazalilla.tiendaCamisetas.repository.RepoCodigoPostal;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/codpos")
public class ControCodigoPostal {

    @Autowired
    private RepoCodigoPostal repoCodigoPostal;

    @GetMapping("/{cp}")
    public ResponseEntity<CodigoPostal> findByCP(
        @PathVariable @NonNull Integer cp) {
        
        List<CodigoPostal> codigoPostal = repoCodigoPostal.findByCodigoPostal(cp);
        if (codigoPostal.size()>0)
            return new ResponseEntity<CodigoPostal>(codigoPostal.get(0), HttpStatus.OK);
        else 
            return new ResponseEntity<CodigoPostal>(HttpStatus.NOT_FOUND);
    }
    
}
