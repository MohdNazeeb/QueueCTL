package org.example.queuectl.cli;

import org.example.queuectl.model.Job;
import org.example.queuectl.model.JobState;
import org.example.queuectl.repository.JobRepository;
import picocli.CommandLine.Command;

import java.util.List;

@Command(
        name = "status",
        description = "Show queue status"
)
public class StatusCommand implements Runnable {

    @Override
    public void run() {

        JobRepository repository = new JobRepository();

        List<Job> jobs = repository.findAll();

        int pending = 0;
        int processing = 0;
        int completed = 0;
        int failed = 0;
        int dead = 0;

        for (Job job : jobs) {

            switch (job.getState()) {

                case PENDING -> pending++;
                case PROCESSING -> processing++;
                case COMPLETED -> completed++;
                case FAILED -> failed++;
                case DEAD -> dead++;
            }
        }

        System.out.println("\nQueue Status");
        System.out.println("----------------------");
        System.out.println("Pending    : " + pending);
        System.out.println("Processing : " + processing);
        System.out.println("Completed  : " + completed);
        System.out.println("Failed     : " + failed);
        System.out.println("Dead       : " + dead);
        System.out.println("Total Jobs : " + jobs.size());
    }
}