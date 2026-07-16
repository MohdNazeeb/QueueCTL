package org.example.queuectl.worker;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Worker {

    public boolean execute(String command) {

        try {

            ProcessBuilder builder = new ProcessBuilder(
                    "cmd.exe", "/c", command
            );

            Process process = builder.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();

            return exitCode == 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }

    }
}