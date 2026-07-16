package org.example.queuectl.repository;
import org.example.queuectl.model.JobState;
import java.time.Instant;
import org.example.queuectl.config.DatabaseConfig;
import org.example.queuectl.model.Job;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class JobRepository {
    public Job claimPendingJob() {

        try (Connection connection = DatabaseConfig.getConnection()) {

            connection.setAutoCommit(false);

            String selectSql = """
                SELECT *
                FROM jobs
                WHERE state = 'PENDING'
                ORDER BY created_at
                LIMIT 1
                """;

            PreparedStatement select = connection.prepareStatement(selectSql);
            ResultSet rs = select.executeQuery();

            if (!rs.next()) {
                connection.commit();
                return null;
            }

            Job job = new Job();
            job.setId(rs.getString("id"));
            job.setCommand(rs.getString("command"));
            job.setState(JobState.valueOf(rs.getString("state")));
            job.setAttempts(rs.getInt("attempts"));
            job.setMaxRetries(rs.getInt("max_retries"));
            job.setCreatedAt(Instant.parse(rs.getString("created_at")));
            job.setUpdatedAt(Instant.parse(rs.getString("updated_at")));

            String updateSql = """
                UPDATE jobs
                SET state = ?,
                    updated_at = ?
                WHERE id = ?
                """;

            PreparedStatement update = connection.prepareStatement(updateSql);

            update.setString(1, JobState.PROCESSING.name());
            update.setString(2, Instant.now().toString());
            update.setString(3, job.getId());

            update.executeUpdate();

            connection.commit();

            return job;

        }catch (Exception e) {

            if (e instanceof java.sql.SQLException &&
                    e.getMessage() != null &&
                    e.getMessage().contains("database is locked")) {
                return null;
            }

            e.printStackTrace();
            return null;
        }
    }
    public boolean retryDeadJob(String id) {

        String sql = """
            UPDATE jobs
            SET state = ?,
                attempts = 0,
                updated_at = ?
            WHERE id = ?
            """;

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, JobState.PENDING.name());
            statement.setString(2, Instant.now().toString());
            statement.setString(3, id);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public List<Job> findDeadJobs() {
        return findByState(JobState.DEAD);
    }
    public List<Job> findByState(JobState state) {

        List<Job> jobs = new ArrayList<>();

        String sql = "SELECT * FROM jobs WHERE state = ? ORDER BY created_at";

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, state.name());

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                Job job = new Job();

                job.setId(rs.getString("id"));
                job.setCommand(rs.getString("command"));
                job.setState(JobState.valueOf(rs.getString("state")));
                job.setAttempts(rs.getInt("attempts"));
                job.setMaxRetries(rs.getInt("max_retries"));
                job.setCreatedAt(Instant.parse(rs.getString("created_at")));
                job.setUpdatedAt(Instant.parse(rs.getString("updated_at")));

                jobs.add(job);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jobs;
    }
    public List<Job> findAll() {

        List<Job> jobs = new ArrayList<>();

        String sql = "SELECT * FROM jobs ORDER BY created_at";

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()
        ) {

            while (rs.next()) {

                Job job = new Job();

                job.setId(rs.getString("id"));
                job.setCommand(rs.getString("command"));
                job.setState(JobState.valueOf(rs.getString("state")));
                job.setAttempts(rs.getInt("attempts"));
                job.setMaxRetries(rs.getInt("max_retries"));
                job.setCreatedAt(Instant.parse(rs.getString("created_at")));
                job.setUpdatedAt(Instant.parse(rs.getString("updated_at")));

                jobs.add(job);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jobs;
    }

    public void incrementAttempts(String id) {

        String sql = """
            UPDATE jobs
            SET attempts = attempts + 1,
                updated_at = ?
            WHERE id = ?
            """;

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, Instant.now().toString());
            statement.setString(2, id);

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Job findById(String id) {

        String sql = "SELECT * FROM jobs WHERE id = ?";

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {

                Job job = new Job();

                job.setId(rs.getString("id"));
                job.setCommand(rs.getString("command"));
                job.setState(JobState.valueOf(rs.getString("state")));
                job.setAttempts(rs.getInt("attempts"));
                job.setMaxRetries(rs.getInt("max_retries"));
                job.setCreatedAt(Instant.parse(rs.getString("created_at")));
                job.setUpdatedAt(Instant.parse(rs.getString("updated_at")));

                return job;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateState(String id, JobState state) {

        String sql = """
            UPDATE jobs
            SET state=?,
                updated_at=?
            WHERE id=?
            """;

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, state.name());
            statement.setString(2, Instant.now().toString());
            statement.setString(3, id);

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void update(Job job) {

        String sql = """
            UPDATE jobs
            SET
                state=?,
                attempts=?,
                updated_at=?,
                next_retry_at=?
            WHERE id=?
            """;

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, job.getState().name());
            statement.setInt(2, job.getAttempts());
            statement.setString(3, job.getUpdatedAt().toString());

            if (job.getNextRetryAt() == null) {
                statement.setNull(4, java.sql.Types.VARCHAR);
            } else {
                statement.setString(4, job.getNextRetryAt().toString());
            }

            statement.setString(5, job.getId());

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Job findPendingJob() {

        String sql = """
            SELECT *
            FROM jobs
            WHERE state = 'PENDING'
            ORDER BY created_at
            LIMIT 1 """;

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()
        ) {

            if (rs.next()) {

                Job job = new Job();

                job.setId(rs.getString("id"));
                job.setCommand(rs.getString("command"));
                job.setState(JobState.valueOf(rs.getString("state")));
                job.setAttempts(rs.getInt("attempts"));
                job.setMaxRetries(rs.getInt("max_retries"));
                job.setCreatedAt(Instant.parse(rs.getString("created_at")));
                job.setUpdatedAt(Instant.parse(rs.getString("updated_at")));

//                String retry = rs.getString("next_retry_at");
//
//                if (retry != null) {
//                    job.setNextRetryAt(Instant.parse(retry));
//                }

                return job;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public void listJobs() {

        String sql = "SELECT * FROM jobs";

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                System.out.println("----------------------------");
                System.out.println("Job ID      : " + resultSet.getString("id"));
                System.out.println("Command     : " + resultSet.getString("command"));
                System.out.println("State       : " + resultSet.getString("state"));
                System.out.println("Attempts    : " + resultSet.getInt("attempts"));
                System.out.println("Max Retries : " + resultSet.getInt("max_retries"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean save(Job job) {

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
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}