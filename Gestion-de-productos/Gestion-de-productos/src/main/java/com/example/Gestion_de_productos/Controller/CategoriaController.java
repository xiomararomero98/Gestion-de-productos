package com.example.Gestion_de_productos.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Gestion_de_productos.Model.Categoria;
import com.example.Gestion_de_productos.Service.CategoriaService;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;


    //obtener todas las categorias
    @GetMapping
    public ResponseEntity<List< Categoria>> obtenerCategoria(){
        List <Categoria> categorias =categoriaService.obtenercategorias();
        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
            
        }       
        return ResponseEntity.ok(categorias);
    }

    //obtener categoria por id

    public ResponseEntity<Categoria> obtenerCategoriaPorId(@PathVariable Long id){
        Optional<Categoria> categoria= categoriaService.obtenerCategoriaPorId(id);
        if (categoria.isEmpty()) {
            return ResponseEntity.notFound().build();
            
        }
        return ResponseEntity.ok(categoria.get());
    }



}
