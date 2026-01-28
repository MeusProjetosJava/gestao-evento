package vitor.gestaoevento.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Table(name = "events")
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalDateTime eventDateTime;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private String attraction;
    @Column(nullable = false)
    private Double price;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;

    public Event() {
    }

    public Event(String name, LocalDateTime eventDateTime, String location,
                 String attraction, Double price, EventStatus status) {
        this.name = name;
        this.eventDateTime = eventDateTime;
        this.location = location;
        this.attraction = attraction;
        this.price = price;
        this.status = status;
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

    public void close() {
        this.status = EventStatus.CLOSED;
    }

    public boolean isActive() {
        return this.status.equals(EventStatus.ACTIVE);
    }
}
