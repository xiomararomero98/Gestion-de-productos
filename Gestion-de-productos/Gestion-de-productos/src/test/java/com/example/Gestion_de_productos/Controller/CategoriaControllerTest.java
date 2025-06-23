package com.example.Gestion_de_productos.Controller;

import com.example.Gestion_de_productos.Model.Categoria;
import com.example.Gestion_de_productos.Service.CategoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriaController.class)
@ExtendWith(SpringExtension.class)
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void obtenerTodas_retornaListaCategorias() throws Exception {
        Categoria categoria = new Categoria(1L, "Perfume para Hombre");
        when(categoriaService.obtenercategorias()).thenReturn(List.of(categoria));

        mockMvc.perform(get("/api/v1/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Perfume para Hombre"));
    }

    @Test
    void obtenerPorId_retornaCategoria() throws Exception {
        Categoria categoria = new Categoria(1L, "Perfume para Hombre");
        when(categoriaService.obtenerCategoriaPorId(1L)).thenReturn(Optional.of(categoria));

        mockMvc.perform(get("/api/v1/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Perfume para Hombre"));
    }

    @Test
    void crearCategoria_retornaCategoriaCreada() throws Exception {
        Categoria categoria = new Categoria(null, "Perfume Unisex");
        Categoria creada = new Categoria(2L, "Perfume Unisex");
        when(categoriaService.crearCategoria(any(Categoria.class))).thenReturn(creada);

        mockMvc.perform(post("/api/v1/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void actualizarCategoria_conIdValido() throws Exception {
        Categoria categoria = new Categoria(1L, "Edición Limitada");
        when(categoriaService.actualizarCategoria(eq(1L), any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(put("/api/v1/categorias/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Edición Limitada"));
    }

    @Test
    void eliminarCategoria_sinErrores() throws Exception {
        doNothing().when(categoriaService).eliminarCategoria(1L);

        mockMvc.perform(delete("/api/v1/categorias/1"))
                .andExpect(status().isNoContent());
    }
}