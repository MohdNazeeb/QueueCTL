package org.example.queuectl.cli;

import picocli.CommandLine.Command;

@Command(
        name = "worker",
        description = "Worker management",
        subcommands = {
                WorkerStartCommand.class,
                WorkerStopCommand.class
        }
)
public class WorkerCommand implements Runnable {

    @Override
    public void run() {
        System.out.println("Use 'worker start' or 'worker stop'");
    }
}