package vitor.gestaoevento.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> realizarChekin(@RequestBody @Valid CheckinRequestDto checkinRequestDto) {
        participacaoService.realizarCheckin(checkinRequestDto.getQrCode());

        return ResponseEntity.noContent().build();
    }
}
