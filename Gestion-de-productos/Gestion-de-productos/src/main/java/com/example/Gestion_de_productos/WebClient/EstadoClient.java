package com.example.Gestion_de_productos.WebClient;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;


@Component
public class EstadoClient {

    private final WebClient webClient;

    public EstadoClient(@Value("${estado-service.url}") String estadoServiceUrl){
        this.webClient =WebClient.builder().baseUrl(estadoServiceUrl).build();
    }

    //METODO PARA REALIZAR CONSULTA URL

    public Map<String, Object> getEstadoById(Long id){
        return this.webClient.get()
               .uri("/{id}", id)
               .retrieve()
               .onStatus(status -> status.is4xxClientError(), 
               response -> response.bodyToMono(String.class)
               .map(body -> new RuntimeException("Cliente no encontrado")))
               .bodyToMono(Map.class).block();
    }

}
