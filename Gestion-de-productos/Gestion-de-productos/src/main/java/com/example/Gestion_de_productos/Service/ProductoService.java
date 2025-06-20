package com.example.Gestion_de_productos.Service;

import com.example.Gestion_de_productos.Model.Categoria;
import com.example.Gestion_de_productos.Model.Producto;
import com.example.Gestion_de_productos.Repository.CategoriaRepository;
import com.example.Gestion_de_productos.Repository.ProductoRepository;
import com.example.Gestion_de_productos.WebClient.EstadoClient;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    // Validar estado con WebClient
    Map<String, Object> estado = estadoClient.getEstadoById(nuevoProducto.getEstadoId());
    if (estado == null || estado.isEmpty()) {
        throw new RuntimeException("Estado no encontrado. No se puede crear el producto");
    }

    // Validar y cargar categoría desde la BD
    Long idCategoria = nuevoProducto.getCategoria().getId();
    Categoria categoriaReal = categoriaRepository.findById(idCategoria)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + idCategoria));

    // Asignar la categoría completa (con nombre)
    nuevoProducto.setCategoria(categoriaReal);

    // Guardar el producto
    Producto guardado = productoRepository.save(nuevoProducto);

    // Asignar el nombre del estado manualmente al final
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
            throw new RuntimeException("Estado no encontrado. No se puede actualizar el producto");
        }

        // Validar y cargar categoría desde base de datos
        Long idCategoria = datosActualizados.getCategoria().getId();
        Categoria categoriaReal = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + idCategoria));

        // Actualizar datos del producto
        existente.setNombre(datosActualizados.getNombre());
        existente.setDescripcion(datosActualizados.getDescripcion());
        existente.setPrecio(datosActualizados.getPrecio());
        existente.setStock(datosActualizados.getStock());
        existente.setCategoria(categoriaReal);
        existente.setEstadoId(datosActualizados.getEstadoId());

        // Guardar y asignar nombre del estado para devolverlo
        Producto actualizado = productoRepository.save(existente);
        actualizado.setNombreEstado((String) estado.get("nombre"));

        return actualizado;
    } else {
        throw new RuntimeException("Producto no encontrado con ID: " + id);
    }
}

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}
