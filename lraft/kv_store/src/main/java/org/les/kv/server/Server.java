package org.les.kv.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.les.core.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final Node node;
    private final Service service;
    private final int port;


    private final NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final NioEventLoopGroup workerGroup = new NioEventLoopGroup(4);

    public Server(Node node, int port) {
        this.node = node;
        this.service = new Service(node);
        this.port = port;
    }

    void start() {

        this.node.start();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new Encoder())
                                .addLast(new Decoder())
                                .addLast(new ServiceHandler(service));
                    }
                });
        serverBootstrap.bind(this.port);
    }

    public void stop() throws InterruptedException {
        this.node.stop();
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
    }
}

