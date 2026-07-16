package org.example.queuectl.worker;

import org.example.queuectl.model.Job;
import org.example.queuectl.model.JobState;
import org.example.queuectl.repository.JobRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkerRunner {

    private final JobRepository repository = new JobRepository();
    private final Worker worker = new Worker();

    public void start(int count) {

        System.out.println("Starting " + count + " worker(s)...");

        ExecutorService executor = Executors.newFixedThreadPool(count);

        for (int i = 1; i <= count; i++) {
            int workerId = i;
            executor.submit(() -> processJobs(workerId));
        }

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void processJobs(int workerId) {

        while (true) {

            Job job = repository.findPendingJob();

            if (job == null) {
                sleep(2000);
                continue;
            }

            repository.updateState(job.getId(), JobState.PROCESSING);

            System.out.println("[Worker " + workerId + "] Executing " + job.getId());

            boolean success = worker.execute(job.getCommand());

            if (success) {
                repository.updateState(job.getId(), JobState.COMPLETED);
                System.out.println("[Worker " + workerId + "] Completed " + job.getId());
            } else {
                repository.updateState(job.getId(), JobState.FAILED);
                System.out.println("[Worker " + workerId + "] Failed " + job.getId());
            }
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}