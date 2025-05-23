package com.example.Gestion_de_productos.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Gestion_de_productos.Model.Producto;
import com.example.Gestion_de_productos.Repository.ProductoRepository;
import com.example.Gestion_de_productos.WebClient.EstadoClient;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private EstadoClient estadoClient;


    //metodo para obtener todos los productos

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


    //metodo para obtener todos los productos por id

    public Optional <Producto> obtenerProductoporId(Long id){
        return productoRepository.findById(id);
    }

    //crear un producto
    public Producto saveProducto(Producto nuevoProducto){
        //verificar el estado EXISTE llamdno al microservicio estado
       Map<String,Object> estado =estadoClient.getEstadoById(nuevoProducto.getEstadoId());
       if (estado ==null ||estado.isEmpty() ) {
        throw new RuntimeException("Estado no encontrado. No se puede crear el producto");
        
       }
       return productoRepository.save(nuevoProducto);
    }

    //metodo para actualizar un producto 
    public Producto actualizarProducto(Long id, Producto datosActualizados){
        Optional<Producto> optional= productoRepository.findById(id);
        if (optional.isPresent()) {
            Producto existente =optional.get();
            existente.setNombre(datosActualizados.getNombre());
            existente.setDescripcion(datosActualizados.getDescripcion());
            existente.setPrecio(datosActualizados.getPrecio());
            existente.setCategoria(datosActualizados.getCategoria());
            existente.setStock(datosActualizados.getStock());
            existente.setEstadoId(datosActualizados.getEstadoId());
            estadoClient.getEstadoById(datosActualizados.getEstadoId());
            return productoRepository.save(existente);
            
        } else {
            throw new RuntimeException("Producto no encontrado con ID:"+ id);

        }


    }

    //metodo para eliminar producto
    public void eliminarProducto(Long id){
        productoRepository.deleteById(id);
    }



}
