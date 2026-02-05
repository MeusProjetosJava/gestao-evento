package vitor.gestaoevento.integration.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vitor.gestaoevento.integration.payment.dto.CreatePaymentResponseDTO;
import vitor.gestaoevento.integration.payment.dto.MercadoPagoWebhookPayload;
import vitor.gestaoevento.integration.payment.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/registrations/{registrationId}")
    public CreatePaymentResponseDTO createPayment(
            @PathVariable Long registrationId) {

        String paymentUrl = paymentService.createPayment(registrationId);
        return new CreatePaymentResponseDTO(paymentUrl);
    }
/*
    @PostMapping("/webhooks/mercadopago")
    public ResponseEntity<Void> receiveWebhook(
            @RequestBody MercadoPagoWebhookPayload payload,
            @RequestHeader("x-signature") String signature) {
        paymentService.processWebhook(payload, signature);
        return ResponseEntity.ok().build();
    }

 */
}
