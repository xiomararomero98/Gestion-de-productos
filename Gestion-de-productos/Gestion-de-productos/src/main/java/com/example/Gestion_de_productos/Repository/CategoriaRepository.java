package com.example.Gestion_de_productos.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Gestion_de_productos.Model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria,Long>{
    Optional<Categoria> findByNombre(String nombre);

}
