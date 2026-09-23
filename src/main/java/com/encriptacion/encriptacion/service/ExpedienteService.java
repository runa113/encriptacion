package com.encriptacion.encriptacion.service;

import com.encriptacion.encriptacion.dto.AgregadorDataResponse;
import com.encriptacion.encriptacion.dto.AgregadorResponse;
import com.encriptacion.encriptacion.dto.ExpedienteClinicoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpedienteService {

    private final ObjectMapper objectMapper;

    //public List<JsonNode> obtenerExpedientes() {
    public AgregadorResponse obtenerExpedientes(ExpedienteClinicoRequestDto request) {

        JsonNode sedena = leerJson("/expedientes/sedena.json");
        JsonNode semar = leerJson("/expedientes/semar.json");


        AgregadorDataResponse data =
                AgregadorDataResponse.builder()
                        .status("COMPLETE")
                        .institucionSolicitante("IMSS")
                        .institucionesConsultadas(
                                List.of("SEDENA", "SEMAR"))
                        .institucionesExitosas(
                                List.of("SEDENA", "SEMAR"))
                        .institucionesFallidas(List.of())
                        .responses(List.of(sedena, semar))
                        .build();

        return AgregadorResponse.builder()
                .status(200)
                .traceId("3c930836-cfe2-47ce-870e-07cd3694be44")
                .timestamp("2026-09-23T16:25:59.801845944Z")
                .data(data)
                .build();

        //return List.of(sedena, semar);
    }

    private JsonNode leerJson(String ruta) {

        try (InputStream inputStream =
                     getClass().getResourceAsStream(ruta)) {

            if (inputStream == null) {
                throw new IllegalStateException(
                        "No se encontró el archivo: " + ruta);
            }

            return objectMapper.readTree(inputStream);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Error al leer el archivo: " + ruta, e);
        }
    }
}
