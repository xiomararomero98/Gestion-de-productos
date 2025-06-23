package com.example.Gestion_de_productos.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo de Producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    @Schema(description = "ID del producto", example = "1")
    private Long id;

    @Column(name = "nombre_producto")
    @Schema(description = "Nombre del producto", example = "Laptop")
    private String nombre;

    @Column
    @Schema(description = "Descripción del producto", example = "Laptop de 15 pulgadas con 16GB de RAM")
    private String descripcion;

    @Column
    @Schema(description = "Precio del producto", example = "1200.00")
    private String precio;

    @Column
    @Schema(description = "Stock del producto", example = "50")
    private String stock;

    @ManyToOne
    @JoinColumn(name = "Categoria_id_categoria", nullable = false)
    @Schema(description = "Categoría del producto")
    private Categoria categoria; 

    @Column(name = "Estado_id_estado")
    @Schema(description = "ID del estado del producto", example = "1")
    private Long estadoId;

    @Transient
    @Schema(description = "Nombre del estado del producto", example = "Disponible")
    private String nombreEstado;
    public String getNombreEstado() {
    return nombreEstado;
}

public void setNombreEstado(String nombreEstado) {
    this.nombreEstado = nombreEstado;
}

}
