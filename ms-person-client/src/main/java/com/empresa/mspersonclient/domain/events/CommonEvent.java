package com.empresa.mspersonclient.domain.events;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

@Getter
public abstract class CommonEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String eventId;
    private final String eventType;
    private final Instant eventTimestamp;

    protected CommonEvent(String eventType) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.eventType = eventType;
        this.eventTimestamp = Instant.now();
    }
}
