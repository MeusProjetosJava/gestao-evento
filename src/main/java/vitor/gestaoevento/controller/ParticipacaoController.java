package vitor.gestaoevento.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vitor.gestaoevento.dto.ParticipacaoRequestDto;
import vitor.gestaoevento.dto.ParticipacaoResponseDto;
import vitor.gestaoevento.model.Participacao;
import vitor.gestaoevento.service.ParticipacaoService;

@Tag(name = "Participações", description = "Gerenciamento de participações em eventos")
@RequestMapping("/participacoes")
@RestController
public class ParticipacaoController {

    ParticipacaoService participacaoService;

    public ParticipacaoController(ParticipacaoService participacaoService) {
        this.participacaoService = participacaoService;
    }

    @Operation(
            summary = "Cadastrar participação",
            description = "Inscreve o usuário autenticado em um evento"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Usuário já inscrito ou evento inválido"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @SecurityRequirement(name = "basicAuth")
    @PostMapping
    public ParticipacaoResponseDto cadastrarParticipacao(@RequestBody ParticipacaoRequestDto
                                                                 participacaoRequestDto) {
        Participacao participacao = participacaoService.cadastrarParticipacao(
                participacaoRequestDto.getEventoId()
        );

        return new ParticipacaoResponseDto(participacao);
    }

    @Operation(
            summary = "Confirmar pagamento",
            description = "Confirma o pagamento de uma participação"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagamento confirmado"),
            @ApiResponse(responseCode = "404", description = "Participação não encontrada"),
            @ApiResponse(responseCode = "400", description = "Pagamento já confirmado")
    })
    @SecurityRequirement(name = "basicAuth")

    @PatchMapping("/{id}/pagamento")
    public ParticipacaoResponseDto confirmarPagamento(@PathVariable Long id) {
        Participacao participacao = participacaoService.confirmarPagamento(id);
        return new ParticipacaoResponseDto(participacao);
    }

    @Operation(
            summary = "Gerar QR Code da participação",
            description = "Gera o QR Code para check-in da participação"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "QR Code gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Pagamento não confirmado ou evento inativo"),
            @ApiResponse(responseCode = "404", description = "Participação não encontrada")
    })
    @SecurityRequirement(name = "basicAuth")
    @GetMapping(value = "/{id}/qrcode",produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> gerarQrCode(@PathVariable Long id) {

        byte[] imagem = participacaoService.gerarQrCodeParticipacao(id);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imagem);
    }


}
