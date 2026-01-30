package vitor.gestaoevento.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void shouldCloseEventWhenItIsActive() {
        Event event = new Event("Show de Rock", LocalDateTime.of(2023, 10, 27, 10, 15, 30),
                "Arena Castelão","Xandy",80.0,EventStatus.ACTIVE);

        event.close();

        assertEquals(EventStatus.CLOSED, event.getStatus());
    }

    @Test
    void isActive() {
    }
}