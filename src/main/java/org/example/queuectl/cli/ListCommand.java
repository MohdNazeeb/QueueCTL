package org.example.queuectl.cli;

import org.example.queuectl.model.Job;
import org.example.queuectl.model.JobState;
import org.example.queuectl.repository.JobRepository;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.List;

@Command(
        name = "list",
        description = "List all jobs"
)
public class ListCommand implements Runnable {

    @Option(
            names = "--state",
            description = "Filter by job state"
    )
    private JobState state;

    @Override
    public void run() {

        JobRepository repository = new JobRepository();

        if (state == null) {
            repository.listJobs();
            return;
        }

        List<Job> jobs = repository.findByState(state);

        if (jobs.isEmpty()) {
            System.out.println("No jobs found.");
            return;
        }

        for (Job job : jobs) {

            System.out.println("----------------------------");
            System.out.println("Job ID      : " + job.getId());
            System.out.println("Command     : " + job.getCommand());
            System.out.println("State       : " + job.getState());
            System.out.println("Attempts    : " + job.getAttempts());
            System.out.println("Max Retries : " + job.getMaxRetries());
        }
    }
}