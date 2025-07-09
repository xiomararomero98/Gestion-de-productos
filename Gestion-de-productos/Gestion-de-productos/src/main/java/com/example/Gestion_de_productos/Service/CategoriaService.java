package com.example.Gestion_de_productos.Service;
import com.example.Gestion_de_productos.Model.Categoria;
import com.example.Gestion_de_productos.Repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Obtener todas las categorías
    public List<Categoria> obtenercategorias() {
        return categoriaRepository.findAll();
    }

    // Obtener categoría por ID
    public Optional<Categoria> obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    // Crear nueva categoría (evita duplicados)
    public Categoria crearCategoria(Categoria categoria) {
        Optional<Categoria> existente = categoriaRepository.findByNombre(categoria.getNombre());
        if (existente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una categoría con ese nombre");
        }
        return categoriaRepository.save(categoria);
    }

    // Actualizar categoría por ID
    public Categoria actualizarCategoria(Long id, Categoria nuevaCategoria) {
        Categoria existente = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada con ID: " + id));
        existente.setNombre(nuevaCategoria.getNombre());
        return categoriaRepository.save(existente);
    }

    // Eliminar categoría por ID
    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada con ID: " + id);
        }
        categoriaRepository.deleteById(id);
    }
}