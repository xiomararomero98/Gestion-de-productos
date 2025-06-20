package com.example.Gestion_de_productos.Model;

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
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @Column(name = "nombre_producto")
    private String nombre;

    @Column
    private String descripcion;

    @Column
    private String precio;

    @Column
    private String stock;

    @ManyToOne
    @JoinColumn(name = "Categoria_id_categoria", nullable = false)
    private Categoria categoria; 

    @Column(name = "Estado_id_estado")
    private Long estadoId;

    @Transient
    private String nombreEstado;
    public String getNombreEstado() {
    return nombreEstado;
}

public void setNombreEstado(String nombreEstado) {
    this.nombreEstado = nombreEstado;
}

}
