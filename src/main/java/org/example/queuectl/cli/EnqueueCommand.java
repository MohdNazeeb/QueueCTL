package org.example.queuectl.cli;

import org.example.queuectl.model.Job;
import org.example.queuectl.util.JsonUtil;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

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

            System.out.println("Job Parsed Successfully!");
            System.out.println("Job ID      : " + job.getId());
            System.out.println("Command     : " + job.getCommand());
            System.out.println("Max Retries : " + job.getMaxRetries());

        } catch (Exception e) {

            System.out.println("Invalid Job JSON!");
            System.out.println(e.getMessage());

        }

    }
}