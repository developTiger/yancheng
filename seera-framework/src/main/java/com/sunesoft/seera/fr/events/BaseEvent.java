package com.sunesoft.seera.fr.events;

import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * Created by zhouz on 2016/5/17.
 */
@MappedSuperclass
public abstract class BaseEvent {
    private UUID eventId;


    private BaseEvent(){}
    public BaseEvent(UUID eId)
    {
        eventId=eId;
    }
    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }
}
