package org.example.queuectl.cli;

import picocli.CommandLine.Command;

@Command(
        name = "dlq",
        description = "Dead Letter Queue commands",
        subcommands = {
                DlqListCommand.class,
                DlqRetryCommand.class
        }
)
public class DlqCommand implements Runnable {

    @Override
    public void run() {
        System.out.println("Use 'dlq list' or 'dlq retry <jobId>'");
    }
}