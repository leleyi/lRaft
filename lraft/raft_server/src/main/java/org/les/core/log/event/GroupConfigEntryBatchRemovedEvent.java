package org.les.core.log.event;

import org.les.core.log.entry.GroupConfigEntry;


public class GroupConfigEntryBatchRemovedEvent {

    private final GroupConfigEntry firstRemovedEntry;

    public GroupConfigEntryBatchRemovedEvent(GroupConfigEntry firstRemovedEntry) {
        this.firstRemovedEntry = firstRemovedEntry;
    }

    public GroupConfigEntry getFirstRemovedEntry() {
        return firstRemovedEntry;
    }

}

