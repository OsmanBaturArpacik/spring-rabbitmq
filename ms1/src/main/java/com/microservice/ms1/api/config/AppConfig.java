package com.microservice.ms1.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class AppConfig {

    @Bean
    public JdbcTemplate jdbcTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");

        try {
            File dbFile = DatabaseInitializer.initializeDatabase();
            String dbPath = dbFile.getAbsolutePath();
            dataSource.setUrl("jdbc:sqlite:" + dbPath);
        } catch (IOException e) {
            throw new RuntimeException("Veritabani dosyasini olustururken bir hata olustu.", e);
        }

        return new JdbcTemplate(dataSource);
    }

    private static class DatabaseInitializer {

        public static File initializeDatabase() throws IOException {
            String resourcePath = "/db/test.db";
            try (InputStream is = DatabaseInitializer.class.getResourceAsStream(resourcePath)) {
                if (is == null) {
                    throw new RuntimeException("Veritabani dosyasi bulunamadi: " + resourcePath);
                }

                Path tempFile = Files.createTempFile("test", ".db");
                try (FileOutputStream os = new FileOutputStream(tempFile.toFile())) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                }

                return tempFile.toFile();
            }
        }
    }
}
