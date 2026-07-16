package org.example.queuectl.cli;

import org.example.queuectl.model.Job;
import org.example.queuectl.repository.JobRepository;
import picocli.CommandLine.Command;

import java.util.List;

@Command(
        name = "list",
        description = "List dead jobs"
)
public class DlqListCommand implements Runnable {

    @Override
    public void run() {

        JobRepository repository = new JobRepository();

        List<Job> jobs = repository.findDeadJobs();

        if (jobs.isEmpty()) {
            System.out.println("Dead Letter Queue is empty.");
            return;
        }

        for (Job job : jobs) {

            System.out.println("----------------------------");
            System.out.println("Job ID      : " + job.getId());
            System.out.println("Command     : " + job.getCommand());
            System.out.println("Attempts    : " + job.getAttempts());
            System.out.println("Max Retries : " + job.getMaxRetries());
        }
    }
}