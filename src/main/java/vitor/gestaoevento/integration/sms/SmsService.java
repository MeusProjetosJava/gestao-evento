package vitor.gestaoevento.integration.sms;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vitor.gestaoevento.model.Participacao;

import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class SmsService {

    @Value("${twilio.sms.from}")
    private String from;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH'h'");

    public void enviarConfirmacaoPagamento(Participacao participacao) {

        String to = participacao.getUsuario().getTelefone();

        String dataFormatada = participacao.getEvento().getDataHoraEvento().format(formatter);

        String mensagem = """
                Olá %s!
                
                Seu pagamento para o evento "%s" foi confirmado com sucesso.
                
                Data: %s
                Local: %s
                
                Bom evento!
                """.formatted(
                participacao.getUsuario().getNome(),
                participacao.getEvento().getAtracao(),
                dataFormatada,
                participacao.getEvento().getLocal()
        );

        Message message = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(from),
                mensagem
        ).create();

    }
}
