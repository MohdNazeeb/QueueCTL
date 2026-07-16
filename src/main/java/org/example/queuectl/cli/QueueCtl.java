package org.example.queuectl.cli;
import org.example.queuectl.cli.WorkerCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
        name = "queuectl",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "A CLI-based Background Job Queue System",
        subcommands = {
                EnqueueCommand.class,
                ListCommand.class,
                WorkerCommand.class,
                StatusCommand.class
        }
)
public class QueueCtl implements Runnable {

    @Override
    public void run() {
        System.out.println("Welcome to QueueCTL");
        System.out.println("Use --help to see available commands.");
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new QueueCtl()).execute(args);
        System.exit(exitCode);
    }
}