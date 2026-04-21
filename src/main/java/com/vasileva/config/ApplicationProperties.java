package com.vasileva.config;

import lombok.SneakyThrows;

import java.io.FileReader;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class ApplicationProperties extends Properties {
    public static final String HIBERNATE_CONNECTION_DRIVER_CLASS = "hibernate.connection.driver_class";
    public static final String ENV_EXPRESSION = "\\$\\{[A-Z_]+:.+}";

    @SneakyThrows
    public ApplicationProperties() {
        this.load(new FileReader(CLASSES_ROOT + "/application.properties"));
        try {
            String driver = this.getProperty(HIBERNATE_CONNECTION_DRIVER_CLASS);
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        scanEnvironmentData();
    }

    private void scanEnvironmentData() {
        this.forEach((key, value) -> {
            String valueString = value.toString();
            Map<String, String> envMap = System.getenv();
            if (valueString.matches(ENV_EXPRESSION)) {
                String[] parts = valueString
                        .replace("${", "")
                        .replace("}", "")
                        .split(":", 2);
                if (parts.length == 2) {
                    String envKey = parts[0];
                    String defaultValue = parts[1];
                    String actualValue = envMap.getOrDefault(envKey, defaultValue);
                    put(key, actualValue);
                }
            }
        });
    }

    public final static Path CLASSES_ROOT = Paths.get(URI.create(
            Objects.requireNonNull(
                    ApplicationProperties.class.getResource("/")
            ).toString()));
}
