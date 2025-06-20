package com.example.Gestion_de_productos.Config;

import com.example.Gestion_de_productos.Model.Categoria;
import com.example.Gestion_de_productos.Model.Producto;
import com.example.Gestion_de_productos.Repository.CategoriaRepository;
import com.example.Gestion_de_productos.Repository.ProductoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoadDataBase {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @PostConstruct
    public void init() {
        // Precargar categorías
        crearCategoriaSiNoExiste("Perfume para Mujer");
        crearCategoriaSiNoExiste("Perfume para Hombre");
        crearCategoriaSiNoExiste("Perfume Unisex");
        crearCategoriaSiNoExiste("Edición Limitada");

        // Precargar productos
        crearProductoSiNoExiste("Perfume Rosa Mística", "Aroma floral suave", "19990", "30", "Perfume para Mujer", 1L);
        crearProductoSiNoExiste("Perfume Aqua Intense", "Fragancia marina intensa", "25990", "50", "Perfume para Hombre", 1L);
    }

    private void crearCategoriaSiNoExiste(String nombre) {
        categoriaRepository.findByNombre(nombre)
            .orElseGet(() -> categoriaRepository.save(new Categoria(null, nombre)));
    }

    private void crearProductoSiNoExiste(String nombre, String descripcion, String precio, String stock, String nombreCategoria, Long estadoId) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findByNombre(nombreCategoria);
        if (categoriaOpt.isPresent()) {
            boolean yaExiste = productoRepository.findByNombre(nombre).isPresent();
            if (!yaExiste) {
                Producto producto = new Producto();
                producto.setNombre(nombre);
                producto.setDescripcion(descripcion);
                producto.setPrecio(precio);
                producto.setStock(stock);
                producto.setCategoria(categoriaOpt.get());
                producto.setEstadoId(estadoId);
                productoRepository.save(producto);
            }
        }
    }
}
