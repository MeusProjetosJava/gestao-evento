package vitor.gestaoevento.integration.payment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vitor.gestaoevento.integration.payment.dto.CreatePaymentRequestDTO;
import vitor.gestaoevento.integration.payment.dto.CreatePaymentResponseDTO;
import vitor.gestaoevento.integration.payment.dto.PaymentWebhookDTO;

@RestController
@RequestMapping("/pagamentos")
public class PaymentController {

    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public CreatePaymentResponseDTO criarPagamento(@RequestBody CreatePaymentRequestDTO
                                                               createPaymentRequestDTO) {
        String paymentUrl = paymentService.criarPagamentoSimulado(
            createPaymentRequestDTO.getParticipacaoId()
        );

        return new CreatePaymentResponseDTO(paymentUrl);
    }

    @PostMapping("/webhook")
    public void receberWebHook(@RequestBody PaymentWebhookDTO paymentWebhookDTO) {
        paymentService.processarWebHookPagamento(paymentWebhookDTO);
    }
}
