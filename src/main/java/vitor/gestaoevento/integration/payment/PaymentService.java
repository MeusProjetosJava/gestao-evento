package vitor.gestaoevento.integration.payment;

import org.springframework.stereotype.Service;
import vitor.gestaoevento.integration.payment.dto.PaymentWebhookDTO;
import vitor.gestaoevento.model.Participacao;
import vitor.gestaoevento.repository.ParticipacaoRepository;
import vitor.gestaoevento.service.ParticipacaoService;

@Service
public class PaymentService {

    private final ParticipacaoService participacaoService;
    private final ParticipacaoRepository participacaoRepository;

    public PaymentService(ParticipacaoRepository participacaoRepository, ParticipacaoService participacaoService) {
        this.participacaoRepository = participacaoRepository;
        this.participacaoService = participacaoService;
    }

    public String criarPagamentoSimulado(Long participacaoId) {
        Participacao participacao = participacaoRepository.findById(participacaoId).orElseThrow(
                () -> new IllegalArgumentException("Participação não encontrada")
        );

        return "https://pagamento-simulado.com/pagar?participacaoId=" + participacao.getId();

    }


    public void processarWebHookPagamento(PaymentWebhookDTO paymentWebhookDTO) {
        if ("approved".equalsIgnoreCase(paymentWebhookDTO.getStatus())) {
            participacaoService.confirmarPagamento(paymentWebhookDTO.getParticipacaoId());
        }
    }
}
