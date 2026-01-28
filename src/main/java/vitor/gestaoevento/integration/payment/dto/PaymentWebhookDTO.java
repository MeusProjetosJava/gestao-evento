package vitor.gestaoevento.integration.payment.dto;

public class PaymentWebhookDTO {

    private Long registrationId;
    private String status;

    public PaymentWebhookDTO() {}

    public String getStatus() {
        return status;
    }

    public Long getRegistrationId() {
        return registrationId;
    }
}
