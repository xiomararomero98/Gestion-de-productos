package com.example.Gestion_de_productos.Controller;
import com.example.Gestion_de_productos.Model.Categoria;
import com.example.Gestion_de_productos.Model.Producto;
import com.example.Gestion_de_productos.Service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(ProductoController.class)
@ExtendWith(SpringExtension.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Categoria categoria = new Categoria(1L, "Fragancias");

    @Test
    void obtenerProductos_devuelveLista() throws Exception {
        Producto producto = new Producto(1L, "Perfume", "Aroma floral", "25000", "10", categoria, 2L, "Disponible");

        when(productoService.getProducto()).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Perfume"))
                .andExpect(jsonPath("$[0].nombreEstado").value("Disponible"));
    }

    @Test
    void obtenerProductoPorId_devuelveProducto() throws Exception {
        Producto producto = new Producto(1L, "Desodorante", "Fresco", "5000", "20", categoria, 1L, "Activo");

        when(productoService.obtenerProductoporId(1L)).thenReturn(Optional.of(producto));

        mockMvc.perform(get("/api/v1/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Desodorante"));
    }

    @Test
    void crearProducto_exitoso() throws Exception {
        Producto producto = new Producto(null, "Loción", "Suave", "18000", "5", categoria, 1L, null);
        Producto creado = new Producto(2L, "Loción", "Suave", "18000", "5", categoria, 1L, "Activo");

        when(productoService.saveProducto(any(Producto.class))).thenReturn(creado);

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("Loción"));
    }

    @Test
void crearProducto_conError() throws Exception {
    Producto producto = new Producto(null, "Loción", "Suave", "18000", "5", categoria, 99L, null);

    when(productoService.saveProducto(any(Producto.class)))
            .thenThrow(new RuntimeException("Estado no válido"));

    mockMvc.perform(post("/api/v1/productos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(producto)))
            .andExpect(status().isConflict()) // usa 409 porque así lo configuraste en el controller
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Error al crear el producto")));
}

    @Test
    void actualizarProducto_exitoso() throws Exception {
        Producto producto = new Producto(1L, "Body Splash", "Refrescante", "7500", "12", categoria, 1L, "Activo");

        when(productoService.actualizarProducto(eq(1L), any(Producto.class))).thenReturn(producto);

        mockMvc.perform(put("/api/v1/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Body Splash"));
    }

    @Test
    void actualizarProducto_conError() throws Exception {
        Producto producto = new Producto(1L, "Loción", "Refrescante", "8000", "15", categoria, 1L, null);

        when(productoService.actualizarProducto(eq(1L), any(Producto.class)))
                .thenThrow(new RuntimeException("Producto no encontrado"));

        mockMvc.perform(put("/api/v1/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Error al actualizar producto")));
    }

    @Test
    void eliminarProducto_exitoso() throws Exception {
        doNothing().when(productoService).eliminarProducto(1L);

        mockMvc.perform(delete("/api/v1/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarProducto_conError() throws Exception {
        doThrow(new RuntimeException("Producto no existe")).when(productoService).eliminarProducto(1L);

        mockMvc.perform(delete("/api/v1/productos/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Error al eliminar producto")));
    }
}