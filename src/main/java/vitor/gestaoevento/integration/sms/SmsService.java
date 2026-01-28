package vitor.gestaoevento.integration.sms;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vitor.gestaoevento.model.Registration;

import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class SmsService {

    @Value("${twilio.sms.from}")
    private String from;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH'h'");

    public void sendPaymentConfirmation(Registration registration) {

        String to = registration.getUser().getPhone();

        String formattedDate = registration.getEvent().getEventDateTime().format(formatter);

        String messageTwilio = """
                Olá %s!
                
                Seu pagamento para o event "%s" foi confirmado com sucesso.
                
                Data: %s
                Local: %s
                
                Bom event!
                """.formatted(
                registration.getUser().getName(),
                registration.getEvent().getAttraction(),
                formattedDate,
                registration.getEvent().getLocation()
        );

        Message message = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(from),
                messageTwilio
        ).create();

    }
}
