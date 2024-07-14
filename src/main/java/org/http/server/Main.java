package org.http.server;

import org.http.server.config.ConfigManager;
import org.http.server.core.ServerConnection;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");
        final ConfigManager configManager= ConfigManager.getInstance();
        configManager.loadConfigs();
        final String port=configManager.getConfigKeyValue("port");
        final ServerConnection serverConnection = ServerConnection.getInstance(Integer.parseInt(port));
        serverConnection.start();
    }
}