package com.example.Gestion_de_productos.Service;

import com.example.Gestion_de_productos.Model.Categoria;
import com.example.Gestion_de_productos.Model.Producto;
import com.example.Gestion_de_productos.Repository.CategoriaRepository;
import com.example.Gestion_de_productos.Repository.ProductoRepository;
import com.example.Gestion_de_productos.WebClient.EstadoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private EstadoClient estadoClient;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoService productoService;

    private Categoria categoria;
    private Producto producto;

    @BeforeEach
    void setUp() {
        categoria = new Categoria(1L, "Perfumes");
        producto = new Producto(1L, "Colonia", "Aroma cítrico", "12000", "15", categoria, 2L, null);
    }

    @Test
    void getProducto_devuelveListaConNombreEstado() {
        when(productoRepository.findAll()).thenReturn(List.of(producto));
        when(estadoClient.getEstadoById(2L)).thenReturn(Map.of("nombre", "Disponible"));

        List<Producto> resultado = productoService.getProducto();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombreEstado()).isEqualTo("Disponible");
    }

    @Test
    void obtenerProductoPorId_devuelveProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> resultado = productoService.obtenerProductoporId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Colonia");
    }

    @Test
    void saveProducto_guardaCorrectamente() {
        when(estadoClient.getEstadoById(2L)).thenReturn(Map.of("nombre", "Nuevo"));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto resultado = productoService.saveProducto(producto);

        assertThat(resultado.getNombre()).isEqualTo("Colonia");
        assertThat(resultado.getCategoria().getNombre()).isEqualTo("Perfumes");
        assertThat(resultado.getNombreEstado()).isEqualTo("Nuevo");
    }

    @Test
    void saveProducto_lanzaErrorSiEstadoNoExiste() {
        when(estadoClient.getEstadoById(2L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                productoService.saveProducto(producto)
        );

        assertThat(exception.getMessage()).contains("Estado no encontrado");
    }

    @Test
    void actualizarProducto_actualizaCorrectamente() {
        Producto actualizado = new Producto(null, "Nuevo Nombre", "Otro", "15000", "20", categoria, 2L, null);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(estadoClient.getEstadoById(2L)).thenReturn(Map.of("nombre", "Activo"));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto resultado = productoService.actualizarProducto(1L, actualizado);

        assertThat(resultado.getNombre()).isEqualTo("Nuevo Nombre");
        assertThat(resultado.getNombreEstado()).isEqualTo("Activo");
    }

@Test
void eliminarProducto_eliminaPorId() {
    Producto producto = new Producto(1L, "Loción", "Suave", "18000", "5", categoria, 1L, null);

    when(productoRepository.existsById(1L)).thenReturn(true);
    doNothing().when(productoRepository).deleteById(1L);

    productoService.eliminarProducto(1L);

    verify(productoRepository, times(1)).deleteById(1L);
}
}