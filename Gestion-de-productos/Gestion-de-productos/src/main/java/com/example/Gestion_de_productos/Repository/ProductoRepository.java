package com.example.Gestion_de_productos.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Gestion_de_productos.Model.Producto;

public interface ProductoRepository extends JpaRepository <Producto, Long> {
    Optional<Producto> findByNombre(String nombre);

}
