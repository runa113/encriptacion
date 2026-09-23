package com.encriptacion.encriptacion.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExpedienteRequest {

    private String curp;

    private String cedulaProfesional;

    private List<String> instituciones;

    private String institucionSolicitante;


}
