package org.example.queuectl.cli;

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

    }
}