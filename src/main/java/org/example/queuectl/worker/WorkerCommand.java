package org.example.queuectl.cli;

import org.example.queuectl.worker.Worker;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(
        name = "worker",
        description = "Execute a command"
)
public class WorkerCommand implements Runnable {

    @Parameters(index = "0")
    private String command;

    @Override
    public void run() {

        Worker worker = new Worker();

        boolean success = worker.execute(command);

        if (success) {
            System.out.println("Command Executed Successfully!");
        } else {
            System.out.println("Command Failed!");
        }

    }
}