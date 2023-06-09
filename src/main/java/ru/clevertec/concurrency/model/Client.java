package ru.clevertec.concurrency.model;

import ru.clevertec.concurrency.data.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Client class which sends multiple request to the server
 *
 * @author Yuryeu Andrei
 */
public class Client {
    /**
     * List of Integers, which are considered to be <b>sent to</b> the server
     */
    private final List<Integer> dataList;
    /**
     * Accumulator, which is considered to store server's responses
     */
    private final AtomicInteger accumulator;
    /**
     * Number of threads, which is equal to dataList size
     */
    private final int threadsNumber;
    private final Lock lock;

    /**
     * Constructor, which fills dataList field and initializes accumulator,
     * threadNumber and lock fields
     */
    public Client(int requestsNumber) {
        this.dataList = new ArrayList<>(IntStream.rangeClosed(1, requestsNumber).boxed().toList());
        this.accumulator = new AtomicInteger(0);
        this.threadsNumber = requestsNumber;
        this.lock = new ReentrantLock();
    }

    /**
     * Method for sending requests to the server, each request is sent by separate thread
     *
     * @param server the server, which requests are considered to be sent to
     * @throws InterruptedException in case of thread interruption
     * @throws ExecutionException   in case of Future counting failure
     * @see Server
     */
    public void sendRequests(Server server) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(threadsNumber);
        List<Future<Integer>> futures = executorService.invokeAll(generateTasks(server));
        for (Future<Integer> future : futures) {
            accumulator.getAndAdd(future.get());
        }
        executorService.shutdown();
    }

    /**
     * Method for generating a list of callable tasks
     *
     * @param server the server, which requests are considered to be sent to
     */
    private List<Callable<Integer>> generateTasks(Server server) {
        Callable<Integer> callable = () -> {
            lock.lock();
            int value;
            try {
                int index = new Random().nextInt(dataList.size());
                value = dataList.remove(index);
            } finally {
                lock.unlock();
            }
            return server.processRequest(new Request(value));
        };
        return IntStream.range(0, threadsNumber)
                .mapToObj(i -> callable)
                .collect(Collectors.toList());
    }

    /**
     * Getter for dataList field
     */
    public List<Integer> getDataList() {
        return dataList;
    }

    /**
     * Getter for accumulator field
     */
    public AtomicInteger getAccumulator() {
        return accumulator;
    }
}
