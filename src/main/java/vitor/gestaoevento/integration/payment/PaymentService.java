package vitor.gestaoevento.integration.payment;

import org.springframework.stereotype.Service;
import vitor.gestaoevento.integration.payment.dto.PaymentWebhookDTO;
import vitor.gestaoevento.model.Registration;
import vitor.gestaoevento.repository.RegistrationRepository;
import vitor.gestaoevento.service.RegistrationService;

@Service
public class PaymentService {

    private final RegistrationService registrationService;
    private final RegistrationRepository registrationRepository;

    public PaymentService(RegistrationRepository registrationRepository, RegistrationService registrationService) {
        this.registrationRepository = registrationRepository;
        this.registrationService = registrationService;
    }

    public String createMockPayment(Long participationId) {
        Registration registration = registrationRepository.findById(participationId).orElseThrow(
                () -> new IllegalArgumentException("Participação não encontrada")
        );

        return "https://pagamento-simulado.com/pagar?participacao=" + registration.getId();

    }


    public void processPaymentWebhook(PaymentWebhookDTO paymentWebhookDTO) {
        if ("approved".equalsIgnoreCase(paymentWebhookDTO.getStatus())) {
            registrationService.confirmPayment(paymentWebhookDTO.getRegistrationId());
        }
    }
}
