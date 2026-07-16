package org.example.queuectl.cli;

import org.example.queuectl.model.Job;
import org.example.queuectl.model.JobState;
import org.example.queuectl.repository.JobRepository;
import org.example.queuectl.util.JsonUtil;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.time.Instant;

@Command(
        name = "enqueue",
        description = "Add a new job to the queue"
)
public class EnqueueCommand implements Runnable {

    @Parameters(
            index = "0",
            description = "Job JSON"
    )
    private String jobJson;

    @Override
    public void run() {

        try {

            Job job = JsonUtil.getMapper().readValue(jobJson, Job.class);

            // Set default values
            job.setState(JobState.PENDING);
            job.setAttempts(0);

            if (job.getMaxRetries() == 0) {
                job.setMaxRetries(3);
            }

            job.setCreatedAt(Instant.now());
            job.setUpdatedAt(Instant.now());

            // Save to database
            JobRepository repository = new JobRepository();
            if (repository.save(job)) {
                System.out.println("Job Enqueued Successfully!");
            } else {
                System.out.println("Failed to enqueue job!");
            }

            System.out.println("\nJob Enqueued Successfully!");
            System.out.println("----------------------------");
            System.out.println("Job ID      : " + job.getId());
            System.out.println("Command     : " + job.getCommand());
            System.out.println("State       : " + job.getState());
            System.out.println("Max Retries : " + job.getMaxRetries());

        } catch (Exception e) {

            System.out.println("Invalid Job JSON!");
            System.out.println(e.getMessage());

        }
    }
}