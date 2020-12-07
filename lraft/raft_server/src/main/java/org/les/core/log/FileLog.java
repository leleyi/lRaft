package org.les.core.log;

import com.google.common.eventbus.EventBus;
import org.les.core.log.entry.EntryMeta;
import org.les.core.log.entry.NoOpEntry;

import java.io.File;

public class FileLog extends AbstractLog {

    public FileLog(File dataDir, EventBus eventBus) {
        super(eventBus);
    }

    @Override
    public NoOpEntry appendEntry(int newTerm) {
        return null;
    }

    @Override
    public EntryMeta getLastEntryMeta() {
        return null;
    }

    @Override
    public int getNextIndex() {
        return 0;
    }

}
