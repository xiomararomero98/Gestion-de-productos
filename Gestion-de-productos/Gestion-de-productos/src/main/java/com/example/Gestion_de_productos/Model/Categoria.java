package com.example.Gestion_de_productos.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categoria")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Modelo de Categoria")
public class Categoria {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    @Schema(description = "ID de la categoría", example = "1")
    private Long id;

    @Column
    @Schema(description = "Nombre de la categoría", example = "Perfumes de mujer")
    private String nombre;

}
