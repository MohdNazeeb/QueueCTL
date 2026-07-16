package org.example.queuectl.cli;

import org.example.queuectl.repository.JobRepository;
import picocli.CommandLine.Command;

@Command(
        name = "list",
        description = "List all jobs"
)
public class ListCommand implements Runnable {

    @Override
    public void run() {
        new JobRepository().listJobs();
    }
}