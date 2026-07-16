package org.example.queuectl.cli;

import picocli.CommandLine.Command;

@Command(
        name = "list",
        description = "List dead jobs"
)
public class DlqListCommand implements Runnable {

    @Override
    public void run() {

    }
}