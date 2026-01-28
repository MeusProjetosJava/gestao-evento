package vitor.gestaoevento.dto;
import vitor.gestaoevento.model.Event;
import vitor.gestaoevento.model.EventStatus;

import java.time.LocalDateTime;

public class EventResponseDto {
    private Long id;
    private String name;
    private LocalDateTime eventDateTime;
    private String location;
    private String attraction;
    private Double price;
    private EventStatus status;

    public EventResponseDto(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.eventDateTime = event.getEventDateTime();
        this.location = event.getLocation();
        this.attraction = event.getAttraction();
        this.price = event.getPrice();
        this.status = event.getStatus();
    }

    public Long getId() {
        return id;
    }

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

    public EventStatus getStatus() {
        return status;
    }
}
