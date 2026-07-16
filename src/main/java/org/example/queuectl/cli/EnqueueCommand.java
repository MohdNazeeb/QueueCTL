package org.example.queuectl.cli;

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

        System.out.println("Received Job:");
        System.out.println(jobJson);

    }
}