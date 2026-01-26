package vitor.gestaoevento.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vitor.gestaoevento.dto.ParticipacaoRequestDto;
import vitor.gestaoevento.dto.ParticipacaoResponseDto;
import vitor.gestaoevento.model.Participacao;
import vitor.gestaoevento.service.ParticipacaoService;

@RequestMapping("/participacoes")
@RestController
public class ParticipacaoController {

    ParticipacaoService participacaoService;

    public ParticipacaoController(ParticipacaoService participacaoService) {
        this.participacaoService = participacaoService;
    }

    @PostMapping
    public ParticipacaoResponseDto cadastrarParticipacao(@RequestBody ParticipacaoRequestDto
                                                                 participacaoRequestDto) {
        Participacao participacao = participacaoService.cadastrarParticipacao(
                participacaoRequestDto.getUsuarioId(),
                participacaoRequestDto.getEventoId()
        );

        return new ParticipacaoResponseDto(participacao);
    }

    @PatchMapping("/{id}/pagamento")
    public ParticipacaoResponseDto confirmarPagamento(@PathVariable Long id) {
        Participacao participacao = participacaoService.confirmarPagamento(id);
        return new ParticipacaoResponseDto(participacao);
    }

    @GetMapping("/{id}/qrcode")
    public ResponseEntity<byte[]> gerarQrCode(@PathVariable Long id) {

        byte[] imagem = participacaoService.gerarQrCodeParticipacao(id);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imagem);
    }


}
