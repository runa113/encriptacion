package com.encriptacion.encriptacion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@JsonPropertyOrder({
        "status",
        "trace_id",
        "timestamp",
        "data"
})
public class AgregadorResponse {

    private Integer status;

    @JsonProperty("trace_id")
    private String traceId;

    private LocalDate timestamp;

    private AgregadorDataResponse data;
}
