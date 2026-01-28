package vitor.gestaoevento.dto;

import vitor.gestaoevento.model.CheckInStatus;

import java.time.LocalDateTime;

public class CheckinResponseDto {

   private CheckInStatus checkInStatus;
   private LocalDateTime checkInDate;

   public CheckinResponseDto(CheckInStatus checkInStatus, LocalDateTime checkInDate) {
       this.checkInStatus = checkInStatus;
       this.checkInDate = checkInDate;
   }

    public CheckInStatus getCheckInStatus() {
        return checkInStatus;
    }

    public LocalDateTime getCheckInDate() {
        return checkInDate;
    }
}
