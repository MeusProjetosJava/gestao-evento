package vitor.gestaoevento.integration.payment.service;

import vitor.gestaoevento.integration.payment.dto.MercadoPagoWebhookPayload;
import vitor.gestaoevento.integration.payment.gateway.MercadoPagoSignatureValidator;
import org.springframework.stereotype.Service;
import vitor.gestaoevento.integration.payment.gateway.MercadoPagoClient;
import vitor.gestaoevento.service.RegistrationService;

@Service
public class PaymentWebhookService {

    private final MercadoPagoClient mercadoPagoClient;
    private final RegistrationService registrationService;
    private final MercadoPagoSignatureValidator signatureValidator;

    public PaymentWebhookService(
            MercadoPagoClient mercadoPagoClient,
            RegistrationService registrationService,
            MercadoPagoSignatureValidator signatureValidator
    ) {
        this.mercadoPagoClient = mercadoPagoClient;
        this.registrationService = registrationService;
        this.signatureValidator = signatureValidator;
    }

    public void handlePaymentNotification(
            MercadoPagoWebhookPayload payload,
            String signature,
            String requestId,
            String dataIdFromQuery
    ) {

        // 🔐 1. Valida assinatura
        signatureValidator.validate(
                signature,
                requestId,
                dataIdFromQuery
        );

        // 🔁 2. Consulta pagamento real
        String paymentId = payload.getData().getId();

        var payment = mercadoPagoClient.getPaymentById(paymentId);

        if (!"approved".equalsIgnoreCase(payment.getStatus())) {
            return;
        }

        Long registrationId = Long.valueOf(payment.getExternalReference());

        registrationService.confirmPayment(registrationId);
    }
}
