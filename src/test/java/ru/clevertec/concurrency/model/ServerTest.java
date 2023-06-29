package ru.clevertec.concurrency.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.concurrency.data.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ServerTest {
    private Server server;

    @BeforeEach
    void setUp() {
        server = new Server();
    }

    @Test
    void checkReceivedDataList_shouldBeEqualToClientsDataList() {
        //given
        int requestsNumber = 100;
        List<Integer> clientsInitialData = new ArrayList<>(IntStream.rangeClosed(1, requestsNumber).boxed().toList());

        //when
        clientsInitialData.stream().parallel().forEach(i -> server.processRequest(new Request(i)));
        List<Integer> serverReceivedData = server.getReceivedData();

        //then
        assertTrue(serverReceivedData.containsAll(clientsInitialData));
    }
}
