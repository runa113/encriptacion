package com.encriptacion.encriptacion.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
/*@JsonPropertyOrder({
        "status",
        "institucion_solicitante",
        "instituciones_consultadas",
        "instituciones_exitosas",
        "instituciones_fallidas",
        "responses"
})*/
public class AgregadorDataResponse {

    private String status;

    private String institucionSolicitante;

    private List<String> institucionesConsultadas;

    private List<String> institucionesExitosas;

    private List<String> institucionesFallidas;

    private List<EncryptedResponseDto> responses;
}
