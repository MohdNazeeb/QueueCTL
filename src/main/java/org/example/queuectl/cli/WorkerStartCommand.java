package org.example.queuectl.cli;

import org.example.queuectl.worker.WorkerRunner;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name = "start",
        description = "Start worker(s)"
)
public class WorkerStartCommand implements Runnable {

    @Option(
            names = "--count",
            defaultValue = "1",
            description = "Number of workers"
    )
    private int count;

    @Override
    public void run() {
        new WorkerRunner().start(count);
    }
}