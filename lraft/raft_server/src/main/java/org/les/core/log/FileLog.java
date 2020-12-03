package org.les.core.log;

import com.google.common.eventbus.EventBus;
import org.les.core.log.entry.EntryMeta;
import org.les.core.log.entry.Log;
import org.les.core.log.entry.NoOpEntry;

import java.io.File;

public class FileLog implements Log {

    public FileLog(File dataDir, EventBus eventBus) {
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
