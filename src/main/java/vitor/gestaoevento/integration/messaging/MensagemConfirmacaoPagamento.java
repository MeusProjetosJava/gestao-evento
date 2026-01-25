package vitor.gestaoevento.integration.messaging;

import vitor.gestaoevento.model.Participacao;

public class MensagemConfirmacaoPagamento {

    public static String gerar(Participacao participacao) {
        return """
                Olá %s!
                
                Seu pagamento para o evento "%s" foi confirmado com sucesso.
                
                Data: %s
                Local: %s
                
                Apresente-se no local com seu check-in.
                Bom evento!
                """.formatted(
                participacao.getUsuario().getNome(),
                participacao.getEvento().getNome(),
                participacao.getEvento().getDataHoraEvento(),
                participacao.getEvento().getLocal()
        );
    }
}
