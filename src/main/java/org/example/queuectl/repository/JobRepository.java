package org.example.queuectl.repository;

import org.example.queuectl.config.DatabaseConfig;
import org.example.queuectl.model.Job;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class JobRepository {

    public void save(Job job) {

        String sql = """
                INSERT INTO jobs(
                    id,
                    command,
                    state,
                    attempts,
                    max_retries,
                    created_at,
                    updated_at
                )
                VALUES(?,?,?,?,?,?,?)
                """;

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, job.getId());
            statement.setString(2, job.getCommand());
            statement.setString(3, job.getState().name());
            statement.setInt(4, job.getAttempts());
            statement.setInt(5, job.getMaxRetries());
            statement.setString(6, job.getCreatedAt().toString());
            statement.setString(7, job.getUpdatedAt().toString());

            statement.executeUpdate();

            System.out.println("Job saved successfully!");

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}