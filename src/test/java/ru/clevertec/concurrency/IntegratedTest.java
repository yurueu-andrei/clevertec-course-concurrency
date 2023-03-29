package ru.clevertec.concurrency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.concurrency.model.Client;
import ru.clevertec.concurrency.model.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegratedTest {
    private Server server;
    private Client client;
    private final int requestsNumber = 1000;

    @BeforeEach
    void setUp() {
        server = new Server();
        client = new Client(requestsNumber);
    }

    @Test
    void checkClientAccumulator_shouldContainSumOfIntegersFrom1ToRequestsNumber() throws ExecutionException, InterruptedException {
        //given
        int expected = (requestsNumber + 1) * requestsNumber / 2;

        //when
        client.sendRequests(server);
        int actual = client.getAccumulator().get();

        //then
        assertEquals(expected, actual);
    }

    @Test
    void checkServerReceivedData_shouldContainAllIntegersFrom1ToRequestsNumberWithoutDuplicates() throws ExecutionException, InterruptedException {
        //given
        List<Integer> expected = new ArrayList<>(requestsNumber);
        for (int i = 1; i <= requestsNumber; i++) {
            expected.add(i);
        }

        //when
        client.sendRequests(server);
        List<Integer> actual = server.getReceivedData();

        //then
        assertTrue(actual.containsAll(expected));
    }
}
