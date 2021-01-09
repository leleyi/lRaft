package org.les.kv.client;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.les.core.node.NodeId;
import org.les.core.rpc.Address;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Console {

    private static final String PROMPT = "kvstore-client " + ">";

    private final Map<String, Command> commandMap;

    private final CommandContext context;

    private final LineReader reader;

    public Console(Map<String, Command> commandMap, CommandContext context, LineReader reader) {
        this.commandMap = commandMap;
        this.context = context;
        this.reader = reader;
    }

    public Console(Map<NodeId, Address> serverMap) {
        commandMap = buildCommandMap(Arrays.asList(new KvStoreGetCommand(), new KvStoreSetCommand()));
        context = new CommandContext(serverMap);

        ArgumentCompleter completer = new ArgumentCompleter(
                new StringsCompleter(commandMap.keySet()),
                new NullCompleter()
        );
        reader = LineReaderBuilder.builder()
                .completer(completer)
                .build();
    }

    private Map<String, Command> buildCommandMap(Collection<Command> commands) {
        Map<String, Command> commandMap = new HashMap<>();
        for (Command cmd : commands) {
            commandMap.put(cmd.getName(), cmd);
        }
        return commandMap;
    }

    void start() {
        context.setRunning(true);
        showInfo();
        String line;
        while (context.isRunning()) {
            try {
                line = reader.readLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                dispatchCommand(line);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showInfo() {
        System.out.println("Welcome to XRaft KVStore Shell\n");
        System.out.println("***********************************************");
        System.out.println("current server list: \n");
        context.printSeverList();
        System.out.println("***********************************************");
    }

    private void dispatchCommand(String line) {
        String[] commandArg = line.split("\\s+", 2);
        String commandName = commandArg[0];
        Command command = commandMap.get(commandName);
        if (command == null) {
            throw new IllegalArgumentException("no such command [ " + commandName + " ]");
        }
        command.execute(commandArg.length > 1 ? commandArg[1] : " ", context);
    }
}
