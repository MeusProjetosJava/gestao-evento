package vitor.gestaoevento.dto;
import java.time.LocalDateTime;

public class EventRequestDto {
    private String name;
    private LocalDateTime eventDateTime;
    private String location;
        private String attraction;
    private Double price;

    public EventRequestDto(){}

    public String getName() {
        return name;
    }

    public LocalDateTime getEventDateTime() {
        return eventDateTime;
    }

    public String getLocation() {
        return location;
    }

    public String getAttraction() {
        return attraction;
    }

    public Double getPrice() {
        return price;
    }
}
