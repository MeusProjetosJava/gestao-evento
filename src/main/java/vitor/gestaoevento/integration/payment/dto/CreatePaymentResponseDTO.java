package vitor.gestaoevento.integration.payment.dto;

public class CreatePaymentResponseDTO {

    private final String paymentUrl;

    public CreatePaymentResponseDTO(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }
}
