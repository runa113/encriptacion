package com.encriptacion.encriptacion.service;

import com.encriptacion.encriptacion.crypto.AadCodec;
import com.encriptacion.encriptacion.crypto.CryptoUtils;
import com.encriptacion.encriptacion.crypto.EncryptedEnvelope;
import com.encriptacion.encriptacion.crypto.HpkeCodec;
import com.encriptacion.encriptacion.dto.AgregadorDataResponse;
import com.encriptacion.encriptacion.dto.AgregadorResponse;
import com.encriptacion.encriptacion.dto.EncryptedResponseDto;
import com.encriptacion.encriptacion.dto.ExpedienteClinicoRequestDto;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.crypto.params.X25519PublicKeyParameters;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpedienteService {

    private final ObjectMapper objectMapper;

    private static final byte[] INFO =
            "secure-demo/v1"
                    .getBytes(StandardCharsets.UTF_8);

    public AgregadorResponse obtenerExpedientes(
            ExpedienteClinicoRequestDto request) throws Exception {

        String traceId = UUID.randomUUID().toString();

        byte[] requesterPublicKeyBytes =
                Base64.getDecoder()
                        .decode(request.getPublicKey());

        JsonNode sedena =
                leerJson("/expedientes/sedena.json");

        JsonNode semar =
                leerJson("/expedientes/sedena.json");

        Map<String, JsonNode> expedientes =
                new LinkedHashMap<>();

        expedientes.put("SEDENA", sedena);
        expedientes.put("SEMAR",semar );

        List<EncryptedResponseDto> responses =
                new ArrayList<>();

        for (Map.Entry<String, JsonNode> entry :
                expedientes.entrySet()) {

            String institucion = entry.getKey();
            JsonNode expediente = entry.getValue();

            EncryptedEnvelope envelope =
                    handle(
                            traceId,
                            institucion,
                            request.getRequester(),
                            requesterPublicKeyBytes,
                            expediente
                    );

            String encBase64 =
                    Base64.getEncoder()
                            .encodeToString(envelope.enc());

            String ctBase64 =
                    Base64.getEncoder()
                            .encodeToString(envelope.ct());

            EncryptedResponseDto encryptedResponse =
                    EncryptedResponseDto.builder()
                            .rid(envelope.rid())
                            .src(envelope.src())
                            .ts(envelope.ts())
                            .enc(encBase64)
                            .ct(ctBase64)
                            .build();

            responses.add(encryptedResponse);
        }

        AgregadorDataResponse data =
                AgregadorDataResponse.builder()
                        .status("COMPLETE")
                        .institucionSolicitante(
                                request.getInstitucionSolicitante())
                        .institucionesConsultadas(
                                List.of("ISSSTE", "IMSS"))
                        .institucionesExitosas(
                                List.of("ISSSTE", "IMSS"))
                        .institucionesFallidas(List.of())
                        .responses(responses)
                        .build();

        return AgregadorResponse.builder()
                .status(200)
                .traceId(traceId)
                .timestamp(LocalDate.now())
                .data(data)
                .build();
    }

    private EncryptedEnvelope handle(
            String requestId,
            String serviceName,
            String requester,
            byte[] requesterPublicKeyBytes,
            JsonNode payload) throws Exception {

        X25519PublicKeyParameters requesterPublicKey =
                CryptoUtils.decodePublicKey(
                        requesterPublicKeyBytes);

        byte[] json =
                objectMapper.writeValueAsBytes(payload);

        long ts =
                Instant.now().getEpochSecond();

        byte[] aad =
                AadCodec.build(
                        requestId,
                        serviceName,
                        requester,
                        ts);

        HpkeCodec.Sealed sealed =
                HpkeCodec.seal(
                        requesterPublicKey,
                        INFO,
                        aad,
                        json);

        return new EncryptedEnvelope(
                requestId,
                serviceName,
                ts,
                sealed.enc(),
                sealed.ciphertext()
        );
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
