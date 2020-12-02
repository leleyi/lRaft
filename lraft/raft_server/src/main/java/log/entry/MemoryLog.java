package log.entry;


import com.google.common.eventbus.EventBus;

public class MemoryLog extends Log {
    private EventBus eventBus;

    public MemoryLog(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}
