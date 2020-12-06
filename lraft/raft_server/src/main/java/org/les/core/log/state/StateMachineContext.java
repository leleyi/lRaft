package org.les.core.log.state;


public interface StateMachineContext {
    void generateSnapshot(int lastIncludedIndex);
}
