package com.encriptacion.encriptacion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ExpedienteClinicoRequestDto {

    /** Clave Única de Registro de Población (CURP) del paciente a consultar. */
    private final String curp;

    /** Cédula profesional del médico que realiza la consulta. */
    private final Integer cedulaProfesional;

    /**
     * Lista de claves de instituciones autorizadas para el usuario.
     *
     * <p>Corresponde a las instituciones activas asociadas al médico según su unidad médica.
     */
    private final List<String> instituciones;

    /**
     * Clave de la institución que origina la solicitud.
     *
     * <p>Generalmente la primera institución activa asociada al usuario autenticado.
     */
    private final String institucionSolicitante;

    /**
     * Public key de la petición que realiza la solicitud.
     */
    private String publicKey;

    private String requester;
}
