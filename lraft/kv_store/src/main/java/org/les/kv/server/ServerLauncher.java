package org.les.kv.server;

import org.apache.commons.cli.*;
import org.les.core.node.Node;
import org.les.core.node.NodeBuilder;
import org.les.core.node.NodeEndpoint;
import org.les.core.node.NodeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerLauncher {

    private static final Logger logger = LoggerFactory.getLogger(ServerLauncher.class);
    private volatile Server server;

    private void execute(String[] args) {
        Options options = new Options();
        options.addOption(Option.builder("i")
                .longOpt("id")
                .hasArg()
                .argName("node-id")
                .required()
                .desc("node id, required. must be unique in group. " +
                        "if starts with mode group-member, please ensure id in group config")
                .build());

        options.addOption(Option.builder("gc")
                .hasArgs()
                .argName("node-endpoint")
                .desc("group config, required when starts with group-member mode. format: <node-endpoint> <node-endpoint>..., " +
                        "format of node-endpoint: <node-id>,<host>,<port-raft-node>, eg: A,localhost,8000 B,localhost,8010")
                .build());

        options.addOption(Option.builder("p2")
                .longOpt("port-service")
                .hasArg()
                .argName("port")
                .type(Number.class)
                .required()
                .desc("port of service, required")
                .build());


        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmdLine = parser.parse(options, args);
            startAsGroupMember(cmdLine);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void startAsGroupMember(CommandLine cmdLine) throws Exception {
        if (!cmdLine.hasOption("gc")) {
            throw new IllegalArgumentException("group-config required");
        }

        String[] rawGroupConfig = cmdLine.getOptionValues("gc");
        String rawNodeId = cmdLine.getOptionValue('i');
        int portService = ((Long) cmdLine.getParsedOptionValue("p2")).intValue();

        Set<NodeEndpoint> nodeEndpoints = Stream.of(rawGroupConfig)
                .map(this::parseNodeEndpoint)
                .collect(Collectors.toSet());

        Node node = new NodeBuilder(nodeEndpoints, new NodeId(rawNodeId)) // node builder 的时候 new a eventBus
                .setDataDir(cmdLine.getOptionValue('d'))
                .build();
        //kv server
        Server server = new Server(node, portService);
        logger.info("start as group member, group config {}, id {}, port service {}", nodeEndpoints, rawNodeId, portService);
        startServer(server);
    }

    /**
     * build EndPoint
     *
     * @param rawNodeEndpoint
     * @return
     */
    private NodeEndpoint parseNodeEndpoint(String rawNodeEndpoint) {
        String[] pieces = rawNodeEndpoint.split(",");
        if (pieces.length != 3) {
            throw new IllegalArgumentException("illegal node endpoint [" + rawNodeEndpoint + "]");
        }
        String nodeId = pieces[0];
        String host = pieces[1];
        int port;
        try {
            port = Integer.parseInt(pieces[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("illegal port in node endpoint [" + rawNodeEndpoint + "]");
        }
        return new NodeEndpoint(nodeId, host, port);
    }

    /**
     * @param server
     */
    private void startServer(Server server) {
        this.server = server;
        this.server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(this::stopServer, "shutdown"));
    }

    private void stopServer() {
        try {
            server.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        // A: -gc A,localhost,2333 B,localhost,2334 C,localhost,2335 -m group-member -i A -p2 3333
        // B: -gc A,localhost,2333 B,localhost,2334 C,localhost,2335 -m group-member -i B -p2 3334
        // C: -gc A,localhost,2333 B,localhost,2334 C,localhost,2335 -m group-member -i A -p2 3335
        ServerLauncher launcher = new ServerLauncher();
        launcher.execute(args);

    }
}
