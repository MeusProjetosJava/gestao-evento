package vitor.gestaoevento.integration.payment.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vitor.gestaoevento.integration.payment.dto.MercadoPagoWebhookPayload;
import vitor.gestaoevento.integration.payment.service.PaymentWebhookService;

@RestController
@RequestMapping("/payments/webhooks/mercadopago")
public class PaymentWebhookController {

    private final PaymentWebhookService webhookService;

    public PaymentWebhookController(PaymentWebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping
    public ResponseEntity<Void> receiveWebhook(
            @RequestBody MercadoPagoWebhookPayload payload,
            @RequestHeader("x-signature") String signature,
            @RequestHeader("x-request-id") String requestId,
            HttpServletRequest request
    ) {

        webhookService.handlePaymentNotification(
                payload,
                signature,
                requestId,
                request.getParameter("data.id")
        );

        return ResponseEntity.ok().build();
    }
}
