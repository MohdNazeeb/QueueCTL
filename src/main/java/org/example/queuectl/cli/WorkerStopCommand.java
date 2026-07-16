package org.example.queuectl.cli;

import picocli.CommandLine.Command;

@Command(
        name = "stop",
        description = "Stop workers"
)
public class WorkerStopCommand implements Runnable {

    @Override
    public void run() {
        System.out.println("Stopping workers...");
        System.exit(0);
    }
}