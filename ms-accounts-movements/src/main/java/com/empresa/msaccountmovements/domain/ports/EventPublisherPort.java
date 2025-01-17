
package com.empresa.msaccountmovements.domain.ports;
import com.empresa.msaccountmovements.domain.events.CommonEvent;

public interface EventPublisherPort {
    void publish(CommonEvent event);
}