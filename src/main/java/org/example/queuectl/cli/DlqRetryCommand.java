package org.example.queuectl.cli;

import org.example.queuectl.repository.JobRepository;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(
        name = "retry",
        description = "Retry a dead job"
)
public class DlqRetryCommand implements Runnable {

    @Parameters(index = "0")
    private String jobId;

    @Override
    public void run() {
        System.out.println("DlqRetryCommand executed");
        JobRepository repository = new JobRepository();

        boolean success = repository.retryDeadJob(jobId);

        if (success) {
            System.out.println("Job moved back to PENDING.");
        } else {
            System.out.println("Job not found.");
        }
    }
}