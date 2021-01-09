package org.les.kv.client;

import org.les.core.node.service.Channel;
import org.les.core.rpc.ChannelException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketChannel implements Channel {

    private final String host;
    private final int port;

    public SocketChannel(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Object send(Object payload) {

        try (Socket socket = new Socket()) {
            socket.setTcpNoDelay(true);
            socket.connect(new InetSocketAddress(this.host, this.port));
            this.write(socket.getOutputStream(), payload);
            return this.read(socket.getInputStream());
        } catch (IOException e) {
            throw new ChannelException("failed to send and receive", e);
        }
    }

    //todo
    private Object read(InputStream inputStream) {
        return null;
    }

    private void write(OutputStream outputStream, Object payload) {
    }

}
