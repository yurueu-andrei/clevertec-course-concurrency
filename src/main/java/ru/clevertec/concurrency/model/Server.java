package ru.clevertec.concurrency.model;

import ru.clevertec.concurrency.data.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Server class which responds to the client with its receivedData size.
 *
 * @author Yuryeu Andrei
 */
public class Server {
    /**
     * List of Integers, which are considered to be <b>received from</b> the client
     */
    private final List<Integer> receivedData;
    private final Lock lock;

    public Server() {
        this.receivedData = new ArrayList<>();
        this.lock = new ReentrantLock();
    }

    /**
     * Method for processing request concurrently using <b>ReentrantLock</b>(timeout - from 100ms to 1s)
     *
     * @param request the request from the client
     * @return <b>receivedData</b> field size
     * @throws InterruptedException in case of thread interruption
     * @see Request
     */
    public int processRequest(Request request) throws InterruptedException {
        lock.lock();
        try {
            Thread.sleep(new Random().nextInt(100, 1001));
            int value = request.getRequestBody();
            receivedData.add(value);
            return receivedData.size();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Getter for receivedData field
     */
    public List<Integer> getReceivedData() {
        return receivedData;
    }
}
