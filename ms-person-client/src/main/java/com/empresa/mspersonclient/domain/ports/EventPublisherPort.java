package com.empresa.mspersonclient.domain.ports;
import com.empresa.mspersonclient.domain.events.CommonEvent;

public interface EventPublisherPort {
    void publish(CommonEvent event);
}
