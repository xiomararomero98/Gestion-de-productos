package com.example.Gestion_de_productos.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.Gestion_de_productos.Model.Categoria;
import com.example.Gestion_de_productos.Repository.CategoriaRepository;

@Configuration
public class LoadDataBase {

    @Bean
    CommandLineRunner initDataBase(CategoriaRepository categoriaRepository){
        return args ->{
            if (categoriaRepository.count()==0) {
                categoriaRepository.save(new Categoria(null, "Perfume para Mujer"));
                categoriaRepository.save(new Categoria(null, "Perfume para Hombre"));
                categoriaRepository.save(new Categoria(null, "Perfume Unisex"));
                categoriaRepository.save(new Categoria(null, "Edición Limitada"));

                System.out.println("Categorias creadas correctamente");

                
            } else {

                System.out.println("Las categorias ya fueron cargadas previamente");
                
            }
        };
    }

}
