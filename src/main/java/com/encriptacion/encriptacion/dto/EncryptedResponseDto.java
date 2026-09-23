package com.encriptacion.encriptacion.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EncryptedResponseDto {

    private String rid;

    private String src;

    private long ts;

    private String enc;

    private String ct;
}
