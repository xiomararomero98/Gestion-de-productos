package com.example.Gestion_de_productos.Service;

import com.example.Gestion_de_productos.Model.Categoria;
import com.example.Gestion_de_productos.Repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria(1L, "Perfume de mujer");
    }

    @Test
    void obtenercategorias_devuelveListaCategorias() {
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        List<Categoria> resultado = categoriaService.obtenercategorias();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Perfume de mujer");
    }

    @Test
    void obtenerCategoriaPorId_devuelveCategoria() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Optional<Categoria> resultado = categoriaService.obtenerCategoriaPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Perfume de mujer");
    }

    @Test
    void crearCategoria_retornaCategoriaGuardada() {
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        Categoria resultado = categoriaService.crearCategoria(categoria);

        assertThat(resultado.getNombre()).isEqualTo("Perfume de mujer");
    }

    @Test
    void actualizarCategoria_conIdValido_actualizaCategoria() {
        Categoria actualizada = new Categoria(null, "Perfume de hombre");
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(actualizada);

        Categoria resultado = categoriaService.actualizarCategoria(1L, actualizada);

        assertThat(resultado.getNombre()).isEqualTo("Perfume de hombre");
    }

@Test
void eliminarCategoria_eliminaCategoria() {
    when(categoriaRepository.existsById(1L)).thenReturn(true);
    doNothing().when(categoriaRepository).deleteById(1L);

    categoriaService.eliminarCategoria(1L);

    verify(categoriaRepository, times(1)).deleteById(1L);
}
}