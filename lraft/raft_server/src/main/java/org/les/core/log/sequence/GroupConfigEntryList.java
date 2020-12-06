package org.les.core.log.sequence;

import org.les.core.log.entry.GroupConfigEntry;

import java.util.Iterator;
import java.util.LinkedList;

public class GroupConfigEntryList implements Iterable<GroupConfigEntry> {

    private final LinkedList<GroupConfigEntry> entries = new LinkedList<>();

    public GroupConfigEntry getLast() {
        return entries.isEmpty() ? null : entries.getLast();
    }

    public void add(GroupConfigEntry entry) {
        entries.add(entry);
    }

    public GroupConfigEntry removeAfter(int entryIndex) {
        Iterator<GroupConfigEntry> iterator = entries.iterator();
        GroupConfigEntry firstRemovedEntry = null;
        while (iterator.hasNext()) {
            GroupConfigEntry entry = iterator.next();
            if (entry.getIndex() > entryIndex) {
                if (firstRemovedEntry == null) {
                    firstRemovedEntry = entry;
                }
                iterator.remove();
            }
        }
        return firstRemovedEntry;
    }


    @Override
    public Iterator<GroupConfigEntry> iterator() {
        return null;
    }

}
