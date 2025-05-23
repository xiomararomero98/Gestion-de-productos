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

import com.example.Gestion_de_productos.Model.Producto;
import com.example.Gestion_de_productos.Service.ProductoService;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> obtenerProductos(){
        List<Producto> productos = productoService.getProducto();
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
            
        }

        return ResponseEntity.ok(productos);

    }

    //obtener producto por id
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id){
        Optional <Producto> producto = productoService.obtenerProductoporId(id);
        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get());
            
        } else {
            return ResponseEntity.notFound().build();
            
        }


    }

    //crear producto
    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto){
        try {
            Producto nuevo =productoService.saveProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el producto"+ e.getMessage());

        }
    }

    //actualizat el producto

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id,@RequestBody Producto productoActualizado ){
        try {
            Optional <Producto> optionalProducto = productoService.obtenerProductoporId(id);
            if (optionalProducto.isPresent()) {
                Producto actualizado = productoService.actualizarProducto(id, productoActualizado);
                return ResponseEntity.ok(actualizado);
                
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto encontrado con ID: "+ id);

                
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar producto"+ e.getMessage());
        }
    }

    //eliminar producto por id 

    @DeleteMapping("/{id}")
    public ResponseEntity <?> eliminarProducto(@PathVariable Long id){
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error al eliminar producto"+ e.getMessage());
        }
    }


}
