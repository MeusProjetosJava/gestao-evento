package vitor.gestaoevento.integration.payment.dto;

public class PaymentWebhookDTO {

    private Long participacaoId;
    private String status;

    public PaymentWebhookDTO() {}

    public String getStatus() {
        return status;
    }

    public Long getParticipacaoId() {
        return participacaoId;
    }
}
