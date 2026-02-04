package vitor.gestaoevento.integration.payment.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vitor.gestaoevento.model.Registration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MercadoPagoClient {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @Value("${mercadopago.webhook-secret}")
    private String webhookSecret;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(accessToken);
    }

    /**
     * Cria um pagamento usando Checkout Pro (Preference API)
     * Retorna o link de pagamento (initPoint)
     */
    public String createPayment(Registration registration) {

        try {
            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .title(registration.getEvent().getName())
                    .quantity(1)
                    .unitPrice(BigDecimal.valueOf(registration.getEvent().getPrice()))
                    .build();

            PreferencePayerRequest payer = PreferencePayerRequest.builder()
                    .email(registration.getUser().getEmail())
                    .build();

            PreferenceRequest request = PreferenceRequest.builder()
                    .items(List.of(item))
                    .payer(payer)
                    .externalReference(registration.getId().toString())
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(request);

            return preference.getInitPoint();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar pagamento no Mercado Pago", e);
        }
    }

    /**
     * Consulta um pagamento real pelo ID recebido via webhook
     */
    public MercadoPagoPayment getPayment(String paymentId) {

        try {
            PaymentClient client = new PaymentClient();
            Payment payment = client.get(Long.parseLong(paymentId));

            return new MercadoPagoPayment(
                    payment.getStatus(),
                    Long.valueOf(payment.getExternalReference())
            );

        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar pagamento no Mercado Pago", e);
        }
    }

    /**
     * Valida a assinatura do webhook do Mercado Pago
     */
    public void validateSignature(Object payload, String signatureHeader) {

        if (signatureHeader == null || signatureHeader.isBlank()) {
            throw new SecurityException("Assinatura do webhook ausente");
        }

        Map<String, String> signatureMap = parseSignatureHeader(signatureHeader);

        String ts = signatureMap.get("ts");
        String v1 = signatureMap.get("v1");

        if (ts == null || v1 == null) {
            throw new SecurityException("Assinatura do webhook inválida");
        }

        try {
            String rawBody = objectMapper.writeValueAsString(payload);
            String signedPayload = ts + "." + rawBody;

            String calculatedHash = hmacSha256(signedPayload, webhookSecret);

            if (!MessageDigest.isEqual(
                    calculatedHash.getBytes(StandardCharsets.UTF_8),
                    v1.getBytes(StandardCharsets.UTF_8))) {

                throw new SecurityException("Assinatura do webhook inválida");
            }

        } catch (Exception e) {
            throw new SecurityException("Erro ao validar assinatura do webhook", e);
        }
    }

    // ==========================
    // MÉTODOS AUXILIARES
    // ==========================

    private Map<String, String> parseSignatureHeader(String header) {
        Map<String, String> values = new HashMap<>();

        String[] parts = header.split(",");

        for (String part : parts) {
            String[] kv = part.split("=");
            if (kv.length == 2) {
                values.put(kv[0].trim(), kv[1].trim());
            }
        }
        return values;
    }

    private String hmacSha256(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey =
                new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        mac.init(secretKey);
        byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder hex = new StringBuilder();
        for (byte b : rawHmac) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}
