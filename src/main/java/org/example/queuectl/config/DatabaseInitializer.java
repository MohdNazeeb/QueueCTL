package org.example.queuectl.config;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {

        String sql = """
                CREATE TABLE IF NOT EXISTS jobs (
                      id TEXT PRIMARY KEY,
                      command TEXT NOT NULL,
                      state TEXT NOT NULL,
                      attempts INTEGER NOT NULL,
                      max_retries INTEGER NOT NULL,
                      created_at TEXT NOT NULL,
                      updated_at TEXT NOT NULL,
                      next_retry_at TEXT
                  );
                """;

        try (
                Connection connection = DatabaseConfig.getConnection();
                Statement statement = connection.createStatement()
        ) {

            statement.execute(sql);

            System.out.println("Jobs table created successfully!");

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}