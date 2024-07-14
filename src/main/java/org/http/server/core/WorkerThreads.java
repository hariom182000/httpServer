package org.http.server.core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;

public class WorkerThreads extends Thread {

    final private String responseStr = "HTTP/1.1 200 OK\r\n" +
            "Content-Type: text/plain\r\n" +
            "Content-Length: 13\r\n" +
            "\r\n" +
            "Hello, World!";

    private Socket socket;

    public WorkerThreads(final Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("Starting new thread " + getName() + " == " + socket.hashCode());
            final OutputStream outputStream = socket.getOutputStream();
            final WritableByteChannel outputChannel = Channels.newChannel(outputStream);

            final ByteBuffer responseBuffer = ByteBuffer.wrap(responseStr.getBytes(StandardCharsets.UTF_8));
            while (responseBuffer.hasRemaining()) {
                outputChannel.write(responseBuffer);
            }
            outputChannel.close();
        } catch (final IOException e) {
            throw new RuntimeException("Error processing HTTP request", e);
        } finally {
            try {
                socket.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }
}
