package vitor.gestaoevento.integration.payment.gateway;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vitor.gestaoevento.model.Registration;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;


import java.math.BigDecimal;
import java.util.List;

@Component
public class MercadoPagoClient {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(accessToken);
        System.out.println("Mercado Pago SDK inicializado");
    }

    /**
     * Cria uma preferência de pagamento (Checkout Pro)
     * Retorna a URL de pagamento (initPoint)
     */
    public String createPayment(Registration registration) {

        try {
            // Item do pagamento (evento)
            PreferenceItemRequest item =
                    PreferenceItemRequest.builder()
                            .title(registration.getEvent().getAttraction())
                            .quantity(1)
                            .unitPrice(
                                    BigDecimal.valueOf(
                                            registration.getEvent().getPrice()
                                    )
                            )
                            .currencyId("BRL")
                            .build();

            // Pagador
            PreferencePayerRequest payer =
                    PreferencePayerRequest.builder()
                            .email(registration.getUser().getEmail())
                            .build();

            // Preferência
            PreferenceRequest request =
                    PreferenceRequest.builder()
                            .items(List.of(item))
                            .payer(payer)
                            .externalReference(
                                    registration.getId().toString()
                            )
                            .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(request);

            return preference.getInitPoint();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao criar preferência de pagamento no Mercado Pago",
                    e
            );
        }
    }
    /**
     * Consulta um pagamento real no Mercado Pago pelo ID
     */
    public Payment getPaymentById(String paymentId) {

        try {
            PaymentClient client = new PaymentClient();
            return client.get(Long.parseLong(paymentId));

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao consultar pagamento no Mercado Pago. paymentId=" + paymentId,
                    e
            );
        }
    }

}
