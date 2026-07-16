package org.example.queuectl.cli;

import org.example.queuectl.worker.WorkerRunner;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name = "worker",
        description = "Start background worker"
)
public class WorkerCommand implements Runnable {

    @Option(
            names = "--count",
            defaultValue = "1",
            description = "Number of workers"
    )
    private int count;

    @Override
    public void run() {

        WorkerRunner runner = new WorkerRunner();

        runner.start(count);

    }
}