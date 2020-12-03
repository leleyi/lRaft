package org.les.core.log.entry;


import com.google.common.eventbus.EventBus;

public class MemoryLog extends AbstractLog {
    private EventBus eventBus;

    public MemoryLog(EventBus eventBus) {
        super(eventBus);
        this.eventBus = eventBus;
    }
}
