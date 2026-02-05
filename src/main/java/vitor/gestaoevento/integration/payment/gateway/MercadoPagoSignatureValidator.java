package vitor.gestaoevento.integration.payment.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

@Component
public class MercadoPagoSignatureValidator {

    @Value("${mercadopago.webhook-secret}")
    private String webhookSecret;

    public void validate(
            String signatureHeader,
            String requestId,
            String dataIdFromQuery
    ) {

        if (signatureHeader == null || signatureHeader.isBlank()) {
            throw new SecurityException("x-signature ausente");
        }

        Map<String, String> signatureMap = parseSignature(signatureHeader);

        String ts = signatureMap.get("ts");
        String v1 = signatureMap.get("v1");

        if (ts == null || v1 == null) {
            throw new SecurityException("Assinatura inválida");
        }

        String manifest =
                "id:" + dataIdFromQuery +
                        ";request-id:" + requestId +
                        ";ts:" + ts + ";";

        String calculatedHash = hmacSha256(manifest, webhookSecret);

        if (!MessageDigest.isEqual(
                calculatedHash.getBytes(StandardCharsets.UTF_8),
                v1.getBytes(StandardCharsets.UTF_8)
        )) {
            throw new SecurityException("Assinatura do webhook inválida");
        }
    }

    private Map<String, String> parseSignature(String header) {
        Map<String, String> map = new HashMap<>();
        String[] parts = header.split(",");

        for (String part : parts) {
            String[] kv = part.split("=");
            if (kv.length == 2) {
                map.put(kv[0].trim(), kv[1].trim());
            }
        }
        return map;
    }

    private String hmacSha256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            ));
            byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : raw) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao validar assinatura", e);
        }
    }
}
