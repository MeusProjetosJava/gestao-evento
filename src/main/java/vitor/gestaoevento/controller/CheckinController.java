package vitor.gestaoevento.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vitor.gestaoevento.dto.CheckinRequestDto;
import vitor.gestaoevento.dto.CheckinResponseDto;
import vitor.gestaoevento.model.Participacao;
import vitor.gestaoevento.service.ParticipacaoService;

@RestController
@RequestMapping("/checkins")
public class CheckinController {

    ParticipacaoService participacaoService;

    public CheckinController(ParticipacaoService ParticipacaoService) {
        this.participacaoService = ParticipacaoService;
    }

    @PostMapping
    public CheckinResponseDto realizarCheckin(@RequestBody CheckinRequestDto checkinRequestDto) {
        Participacao participacao = participacaoService.realizarCheckin(checkinRequestDto.getUsuarioId(),
                checkinRequestDto.getEventoId());

        return new CheckinResponseDto(participacao.getStatusCheckin(), participacao.getDataCheckin());
    }
}
