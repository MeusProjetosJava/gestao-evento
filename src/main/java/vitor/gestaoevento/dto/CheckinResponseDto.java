package vitor.gestaoevento.dto;

import vitor.gestaoevento.model.CheckInStatus;

import java.time.LocalDateTime;

public class CheckinResponseDto {

   private CheckInStatus checkInStatus;
   private LocalDateTime dataCheckin;

   public CheckinResponseDto(CheckInStatus checkInStatus, LocalDateTime dataCheckin) {
       this.checkInStatus = checkInStatus;
       this.dataCheckin = dataCheckin;
   }

    public CheckInStatus getCheckInStatus() {
        return checkInStatus;
    }

    public LocalDateTime getDataCheckin() {
        return dataCheckin;
    }
}
