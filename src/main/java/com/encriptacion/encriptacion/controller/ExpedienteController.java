package com.encriptacion.encriptacion.controller;

import com.encriptacion.encriptacion.dto.AgregadorResponse;
import com.encriptacion.encriptacion.dto.ExpedienteClinicoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.encriptacion.encriptacion.service.ExpedienteService;
import tools.jackson.databind.JsonNode;

import java.util.List;

@RestController
@RequestMapping("/exp")
@RequiredArgsConstructor
public class ExpedienteController {

    private final ExpedienteService expedienteService;

    @GetMapping("/instituciones")
    @PostMapping("/instituciones")
    public ResponseEntity<AgregadorResponse> obtenerExpedientes(
            @RequestBody ExpedienteClinicoRequestDto request
    ) {

        AgregadorResponse response =
                expedienteService.obtenerExpedientes(request);
        return ResponseEntity.ok(response);
    }

/*    public ResponseEntity<List<JsonNode>> obtenerExpedientes() {

        List<JsonNode> expedientes =
                expedienteService.obtenerExpedientes();
        return ResponseEntity.ok(expedientes);
    }*/
}
