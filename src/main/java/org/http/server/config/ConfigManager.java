package org.http.server.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigManager {
    private static ConfigManager configManager;
    private Properties properties;

    public static ConfigManager getInstance() {
        if (Objects.isNull(configManager)) configManager = new ConfigManager();
        return configManager;
    }


    /* loads .properties file in chunks */
    public void loadConfigs() {
        properties = new Properties();
        final Path filePath = Paths.get("src/main/resources/application.properties");
        try (final FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024); // 1KB buffer size
            while (fileChannel.read(buffer) > 0) {
                buffer.flip();
                properties.load(new ByteArrayInputStream(buffer.array()));
                buffer.clear();
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* reads key's value from properties map */
    public String getConfigKeyValue(final String key) {
        String value = properties.getProperty(key);
        if (Objects.isNull(value) || value.isEmpty()) {
            //log.warn("value not present for key {}", key);
            return value;
        }
        return value.trim();
    }
}
