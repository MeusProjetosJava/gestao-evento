package vitor.gestaoevento.dto;

import vitor.gestaoevento.model.StatusCheckin;

import java.time.LocalDateTime;

public class CheckinResponseDto {

   private StatusCheckin statusCheckin;
   private LocalDateTime dataCheckin;

   public CheckinResponseDto(StatusCheckin statusCheckin, LocalDateTime dataCheckin) {
       this.statusCheckin = statusCheckin;
       this.dataCheckin = dataCheckin;
   }

    public StatusCheckin getStatusCheckin() {
        return statusCheckin;
    }

    public LocalDateTime getDataCheckin() {
        return dataCheckin;
    }
}
