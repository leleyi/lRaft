package org.les.core.log.entry;

public interface Entry {

    int KIND_NO_OP = 0;
    int KIND_GENERAL = 1;
    int KIND_ADD_NODE = 3;
    int KIND_REMOVE_NODE = 4;

    public int getKind();


    public int getIndex();

    public int getTerm();

    public byte[] getCommandBytes();
}
