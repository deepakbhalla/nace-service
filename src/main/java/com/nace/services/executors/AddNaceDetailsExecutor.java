package com.nace.services.executors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.nace.entities.NaceDetailsEntity;
import com.nace.repositories.NaceDetailsRepository;

/**
 * Executor service for adding nace details to database.
 * 
 * @author Deepak Bhalla
 *
 */
@Component
public class AddNaceDetailsExecutor {

    /**
     * Partition size.
     */
    private static final int PARTITION_SIZE = 500;

    /**
     * Executor.
     * 
     * @param naceDetails           - List of NaceDetailsEntity - List of entities
     *                              to be persisted
     * @param naceDetailsRepository - NaceDetailsRepository - JPA Repository
     * @return result - List of NaceDetailsEntity - Persisted list of entities
     * @throws InterruptedException
     */
    public List<NaceDetailsEntity> execute(List<NaceDetailsEntity> naceDetails, NaceDetailsRepository naceDetailsRepository)
            throws InterruptedException {

        List<NaceDetailsEntity> result;
        final AtomicInteger counter = new AtomicInteger(0);
        Collection<List<NaceDetailsEntity>> partitionedNaceDetails = naceDetails.stream()
                .collect(Collectors.groupingBy(s -> counter.getAndIncrement() / PARTITION_SIZE)).values();

        ExecutorService executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

        List<Callable<List<NaceDetailsEntity>>> callables = new ArrayList<>();
        partitionedNaceDetails.forEach(sublist -> {
            callables.add(new AddNaceDetailsTask(sublist, naceDetailsRepository));
        });

        Stream<List<NaceDetailsEntity>> map = executor.invokeAll(callables).stream().map(future -> {
            try {
                return future.get();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });

        result = map.flatMap(List::stream).collect(Collectors.toList());
        shudownExecutorService(executor);
        return result;
    }

    /**
     * Shutdowns ExecutorService.
     * 
     * @param executor - ExecutorService
     */
    private void shudownExecutorService(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
