package com.example.Gestion_de_productos.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Gestion_de_productos.Model.Categoria;
import com.example.Gestion_de_productos.Repository.CategoriaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    //metodo para obtener todas las categorias

    public List <Categoria> obtenercategorias(){
        return categoriaRepository.findAll();
    }

    //metodo para obtener categorias por id

    public Optional<Categoria> obtenerCategoriaPorId(Long id){
        return categoriaRepository.findById(id);
    }

    //metodo para crear una categoria

    public Categoria crearCategoria(Categoria categoria){
        return categoriaRepository.save(categoria);
    }

    //metodo para actualizar una categoria por id
     public Categoria actualizarCategoria(Long id, Categoria nuevaCategoria){
        Optional<Categoria> optional = categoriaRepository.findById(id);
        if (optional.isPresent()) {
            Categoria existente = optional.get();
            existente.setNombre(nuevaCategoria.getNombre());
            return categoriaRepository.save(existente);
            
        } else {
            throw new RuntimeException("Categoria no encontrada con ID;"+ id);
            
        }
     }

     //metodo para eliminar una categoria por id

     public void eliminarCategoria(Long id){
        categoriaRepository.deleteById(id);

     }


}
