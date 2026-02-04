package vitor.gestaoevento.integration.payment.gateway;

public class MercadoPagoPayment {

    private final String status;
    private final Long externalReference;

    public MercadoPagoPayment(String status, Long externalReference) {
        this.status = status;
        this.externalReference = externalReference;
    }

    public String getStatus() {
        return status;
    }

    public Long getExternalReference() {
        return externalReference;
    }
}
