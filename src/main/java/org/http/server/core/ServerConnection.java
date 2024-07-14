package org.http.server.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerConnection extends Thread {

    public static ServerConnection serverConnection;

    private int port;

    public static ServerConnection getInstance(final int port) throws IOException {
        if (Objects.isNull(serverConnection)) serverConnection = new ServerConnection(port);
        return serverConnection;
    }

    private ServerConnection(final int port) throws IOException {
        this.port = port;
    }

    @Override
    public void run() {
        try (final ServerSocket serverSocket = new ServerSocket(port)) {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    final WorkerThreads workerThread = new WorkerThreads(socket);
                    workerThread.start();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
