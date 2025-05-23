package com.example.Gestion_de_productos.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping({"/{id}"})
    public ResponseEntity<Categoria> obtenerCategoriaPorId(@PathVariable Long id){
        Optional<Categoria> categoria= categoriaService.obtenerCategoriaPorId(id);
        if (categoria.isEmpty()) {
            return ResponseEntity.notFound().build();
            
        }
        return ResponseEntity.ok(categoria.get());
    }

    //Crear categoria
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Categoria categoria){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crearCategoria(categoria));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear categoria"+ e.getMessage());
        }

    }

    //Actualizar categoria por id
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Categoria categoria){
        try {
            return ResponseEntity.ok(categoriaService.actualizarCategoria(id, categoria));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizat la categoria"+ e.getMessage());
        }
    }

    //Borrar categoria por id
    @DeleteMapping("/{id}")
    public ResponseEntity<?>eliminar(@PathVariable Long id){
        try {
            categoriaService.eliminarCategoria(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error al borrar categoria"+ e.getMessage());
        }
    }




}
