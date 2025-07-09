package com.example.Gestion_de_productos.Service;
import com.example.Gestion_de_productos.Model.Categoria;
import com.example.Gestion_de_productos.Model.Producto;
import com.example.Gestion_de_productos.Repository.CategoriaRepository;
import com.example.Gestion_de_productos.Repository.ProductoRepository;
import com.example.Gestion_de_productos.WebClient.EstadoClient;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private EstadoClient estadoClient;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Producto> getProducto() {
        List<Producto> productos = productoRepository.findAll();
        for (Producto producto : productos) {
            Map<String, Object> estado = estadoClient.getEstadoById(producto.getEstadoId());
            if (estado != null && estado.containsKey("nombre")) {
                producto.setNombreEstado((String) estado.get("nombre"));
            } else {
                producto.setNombreEstado("Desconocido");
            }
        }
        return productos;
    }

    public Optional<Producto> obtenerProductoporId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto saveProducto(Producto nuevoProducto) {
        // Validación: evitar sobrescritura si el producto tiene un ID existente
        if (nuevoProducto.getId() != null && productoRepository.existsById(nuevoProducto.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un producto con ese ID.");
        }

        // Validación por nombre (opcional, puedes quitar si no aplica)
        Optional<Producto> productoExistente = productoRepository.findByNombre(nuevoProducto.getNombre());
        if (productoExistente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un producto con ese nombre.");
        }

        // Validar estado con microservicio externo
        Map<String, Object> estado = estadoClient.getEstadoById(nuevoProducto.getEstadoId());
        if (estado == null || estado.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado no encontrado. No se puede crear el producto.");
        }

        // Validar categoría existente
        Long idCategoria = nuevoProducto.getCategoria().getId();
        Categoria categoriaReal = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría no encontrada con ID: " + idCategoria));

        nuevoProducto.setCategoria(categoriaReal);
        Producto guardado = productoRepository.save(nuevoProducto);
        guardado.setNombreEstado((String) estado.get("nombre"));

        return guardado;
    }

    public Producto actualizarProducto(Long id, Producto datosActualizados) {
        Optional<Producto> optional = productoRepository.findById(id);
        if (optional.isPresent()) {
            Producto existente = optional.get();

            // Validar estado desde microservicio Estado
            Map<String, Object> estado = estadoClient.getEstadoById(datosActualizados.getEstadoId());
            if (estado == null || estado.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado no encontrado. No se puede actualizar el producto.");
            }

            // Validar categoría
            Long idCategoria = datosActualizados.getCategoria().getId();
            Categoria categoriaReal = categoriaRepository.findById(idCategoria)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría no encontrada con ID: " + idCategoria));

            // Actualizar datos
            existente.setNombre(datosActualizados.getNombre());
            existente.setDescripcion(datosActualizados.getDescripcion());
            existente.setPrecio(datosActualizados.getPrecio());
            existente.setStock(datosActualizados.getStock());
            existente.setCategoria(categoriaReal);
            existente.setEstadoId(datosActualizados.getEstadoId());

            Producto actualizado = productoRepository.save(existente);
            actualizado.setNombreEstado((String) estado.get("nombre"));

            return actualizado;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con ID: " + id);
        }
    }

    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }
}