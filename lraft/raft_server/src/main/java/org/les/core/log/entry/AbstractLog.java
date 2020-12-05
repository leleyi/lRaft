package org.les.core.log.entry;

import com.google.common.eventbus.EventBus;
import org.les.core.log.sequence.EntrySequence;
import org.les.core.log.snapshot.EntryInSnapshotException;
import org.les.core.log.snapshot.Snapshot;
import org.les.core.log.snapshot.SnapshotChunk;
import org.les.core.log.state.EmptyStateMachine;
import org.les.core.node.NodeId;
import org.les.core.log.state.StateMachine;
import org.les.core.rpc.message.AppendEntriesRpc;
import org.les.core.rpc.message.InstallSnapshotRpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

abstract class AbstractLog implements Log {

    private static final Logger logger = LoggerFactory.getLogger(AbstractLog.class);
    protected final EventBus eventBus;
    protected Snapshot snapshot;
    protected EntrySequence entrySequence;
    protected int commitIndex = 0;
    protected StateMachine stateMachine = new EmptyStateMachine();


    AbstractLog(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public NoOpEntry appendEntry(int newTerm) {
        return null;
    }


    @Override
    public EntryMeta getLastEntryMeta() {
        if (entrySequence.isEmpty()) {
            return new EntryMeta(Entry.KIND_NO_OP, snapshot.getLastIncludedIndex(), snapshot.getLastIncludedTerm());
        }
        return entrySequence.getLastEntry().getMeta();
    }


    @Override
    public int getNextIndex() {
        return 0;
    }

    @Override
    public boolean isNewerThan(int lastLogIndex, int lastLogTerm) {
        EntryMeta lastEntryMeta = getLastEntryMeta();
        logger.debug("last entry ({}, {}), candidate ({}, {})", lastEntryMeta.getIndex(), lastEntryMeta.getTerm(), lastLogIndex, lastLogTerm);
        return lastEntryMeta.getTerm() > lastLogTerm || lastEntryMeta.getIndex() > lastLogIndex;
    }

    /**
     * commit a new entry
     *
     * @param term
     * @param selfId
     * @param nextIndex
     * @param maxEntries
     * @return
     */
    @Override
    public AppendEntriesRpc createAppendEntriesRpc(int term, NodeId selfId, int nextIndex, int maxEntries) {

        int nextLogIndex = entrySequence.getNextLogIndex();
        if (nextIndex > nextLogIndex) {
            throw new IllegalArgumentException("illegal next index " + nextIndex);
        }
        if (nextIndex <= snapshot.getLastIncludedIndex()) {
            throw new EntryInSnapshotException(nextIndex);
        }
        AppendEntriesRpc rpc = new AppendEntriesRpc();
        rpc.setMessageId(UUID.randomUUID().toString());
        rpc.setTerm(term);
        rpc.setLeaderId(selfId);
        rpc.setLeaderCommit(commitIndex);
        // according to the next index . Decide send what entries info.
        // 1.from snapshot. 2.from current entry 3. or a list of entries
        if (nextIndex == snapshot.getLastIncludedIndex() + 1) {
            rpc.setPrevLogIndex(snapshot.getLastIncludedIndex());
            rpc.setPrevLogTerm(snapshot.getLastIncludedTerm());
        } else {
            Entry entry = entrySequence.getEntry(nextIndex - 1);
            assert entry != null;
            rpc.setPrevLogIndex(entry.getIndex());
            rpc.setPrevLogTerm(entry.getTerm());
        }
        if (!entrySequence.isEmpty()) {
            int maxIndex = (maxEntries == ALL_ENTRIES ? nextLogIndex : Math.min(nextLogIndex, nextIndex + maxEntries));
            rpc.setEntries(entrySequence.subList(nextIndex, maxIndex));
        }
        return rpc;
    }

    @Override
    public InstallSnapshotRpc createInstallSnapshotRpc(int term, NodeId selfId, int offset, int length) {
        InstallSnapshotRpc rpc = new InstallSnapshotRpc();
        rpc.setTerm(term);
        rpc.setLeaderId(selfId);
        rpc.setLastIndex(snapshot.getLastIncludedIndex());
        rpc.setLastTerm(snapshot.getLastIncludedTerm());
        if (offset == 0) {
            rpc.setLastConfig(snapshot.getLastConfig());
        }
        rpc.setOffset(offset);

        SnapshotChunk chunk = snapshot.readData(offset, length);
        rpc.setData(chunk.toByteArray());
        rpc.setDone(chunk.isLastChunk());
        return rpc;
    }

    @Override
    public void advanceCommitIndex(int newCommitIndex, int currentTerm) {

    }


    @Override
    public boolean appendEntriesFromLeader(int prevLogIndex, int prevLogTerm, List<Entry> leaderEntries) {
        // check previous log
        if (!checkIfPreviousLogMatches(prevLogIndex, prevLogTerm)) {
            return false;
        }
        // heartbeat no-op appendEntries
        if (leaderEntries.isEmpty()) {
            return true;
        }
//        assert prevLogIndex + 1 == leaderEntries.get(0).getIndex(); //
        EntrySequenceView newEntries = removeUnmatchedLog(new EntrySequenceView(leaderEntries));
        appendEntriesFromLeader(newEntries);
        return true;
    }

    /**
     * after the first not match log. all the logs later also not match.
     *
     * @param leaderEntries
     * @return
     */
    private EntrySequenceView removeUnmatchedLog(EntrySequenceView leaderEntries) {
        assert !leaderEntries.isEmpty();
        int firstUnmatched = findFirstUnmatchedLog(leaderEntries);
        removeEntriesAfter(firstUnmatched - 1);
        return leaderEntries.subView(firstUnmatched);
    }


    /**
     * return the first not match index;
     *
     * @param leaderEntries
     * @return
     */
    private int findFirstUnmatchedLog(EntrySequenceView leaderEntries) {
        assert !leaderEntries.isEmpty();
        int logIndex;
        EntryMeta followerEntryMeta;
        for (Entry leaderEntry : leaderEntries) {
            logIndex = leaderEntry.getIndex();
            followerEntryMeta = entrySequence.getEntryMeta(logIndex);
            if (followerEntryMeta == null || followerEntryMeta.getTerm() != leaderEntry.getTerm()) {
                return logIndex;
            }
        }
        return leaderEntries.getLastLogIndex() + 1;
    }

    private void removeEntriesAfter(int index) {
//        if (entrySequence.isEmpty() || index >= entrySequence.getLastLogIndex()) {
//            return;
//        }
//        int lastApplied = stateMachine.getLastApplied();
//        if (index < lastApplied && entrySequence.subList(index + 1, lastApplied + 1).stream().anyMatch(this::isApplicable)) {
//            logger.warn("applied log removed, reapply from start");
//            applySnapshot(snapshot);
//            logger.debug("apply log from {} to {}", entrySequence.getFirstLogIndex(), index);
//            entrySequence.subList(entrySequence.getFirstLogIndex(), index + 1).forEach(this::applyEntry);
//        }
//        logger.debug("remove entries after {}", index);
//        entrySequence.removeAfter(index);
//        if (index < commitIndex) {
//            commitIndex = index;
//        }
//        GroupConfigEntry firstRemovedEntry = groupConfigEntryList.removeAfter(index);
//        if (firstRemovedEntry != null) {
//            logger.info("group config removed");
//            eventBus.post(new GroupConfigEntryBatchRemovedEvent(firstRemovedEntry));
//        }
    }


    private boolean checkIfPreviousLogMatches(int prevLogIndex, int prevLogTerm) {
        int lastIncludedIndex = snapshot.getLastIncludedIndex();
        if (prevLogIndex < lastIncludedIndex) {
            logger.debug("previous log index {} < snapshot's last included index {}", prevLogIndex, lastIncludedIndex);
            return false;
        }
        if (prevLogIndex == lastIncludedIndex) {
            int lastIncludedTerm = snapshot.getLastIncludedTerm();
            if (prevLogTerm != lastIncludedTerm) {
                logger.debug("previous log index matches snapshot's last included index, " +
                        "but term not (expected {}, actual {})", lastIncludedTerm, prevLogTerm);
                return false;
            }
            return true;
        }
        Entry entry = entrySequence.getEntry(prevLogIndex);
        if (entry == null) {
            logger.debug("previous log {} not found", prevLogIndex);
            return false;
        }
        int term = entry.getTerm();
        if (term != prevLogTerm) {
            logger.debug("different term of previous log, local {}, remote {}", term, prevLogTerm);
            return false;
        }
        return true;
    }


    private void appendEntriesFromLeader(EntrySequenceView newEntries) {

    }

    /**
     *
     */
    private static class EntrySequenceView implements Iterable<Entry> {

        private final List<Entry> entries;
        private int firstLogIndex;
        private int lastLogIndex;

        EntrySequenceView(List<Entry> entries) {
            this.entries = entries;
            if (!entries.isEmpty()) {
                firstLogIndex = entries.get(0).getIndex();
                lastLogIndex = entries.get(entries.size() - 1).getIndex();
            }
        }

        Entry get(int index) {
            if (entries.isEmpty() || index < firstLogIndex || index > lastLogIndex) {
                return null;
            }
            return entries.get(index - firstLogIndex);
        }

        boolean isEmpty() {
            return entries.isEmpty();
        }

        int getFirstLogIndex() {
            return firstLogIndex;
        }

        int getLastLogIndex() {
            return lastLogIndex;
        }

        EntrySequenceView subView(int fromIndex) {
            if (entries.isEmpty() || fromIndex > lastLogIndex) {
                return new EntrySequenceView(Collections.emptyList());
            }
            return new EntrySequenceView(
                    entries.subList(fromIndex - firstLogIndex, entries.size())
            );
        }

        @Override
        @Nonnull
        public Iterator<Entry> iterator() {
            return entries.iterator();
        }
    }

}
