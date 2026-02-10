package vitor.gestaoevento.integration.payment.service;

import org.springframework.stereotype.Service;
import vitor.gestaoevento.integration.payment.dto.MercadoPagoWebhookPayload;
import vitor.gestaoevento.integration.payment.gateway.MercadoPagoClient;
import vitor.gestaoevento.integration.payment.gateway.MercadoPagoPayment;
import vitor.gestaoevento.model.Registration;
import vitor.gestaoevento.service.RegistrationService;

@Service
public class PaymentService {

    private final RegistrationService registrationService;
    private final MercadoPagoClient mercadoPagoClient;

    public PaymentService(
            RegistrationService registrationService,
            MercadoPagoClient mercadoPagoClient) {

        this.registrationService = registrationService;
        this.mercadoPagoClient = mercadoPagoClient;
    }

    public String createPayment(Long registrationId) {

        Registration registration =
                registrationService.findById(registrationId);

        if (registration.isPaid()) {
            throw new IllegalStateException("Registration already paid");
        }

        return mercadoPagoClient.createPayment(registration);
    }

}
