package ru.clevertec.concurrency.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.concurrency.data.Request;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClientTest {
    private Client client;
    @Mock
    private Server server;
    private final int requestsNumber = 1000;

    @BeforeEach
    void setUp() {
        client = new Client(requestsNumber);
    }

    @Test
    void checkDataListSize_shouldBeEmpty() throws ExecutionException, InterruptedException {
        //when
        doReturn(0).when(server).processRequest(any(Request.class));
        client.sendRequests(server);

        //then
        assertTrue(client.getDataList().isEmpty());
    }

    @Test
    void checkSentRequests_serverShouldProcessEachRequest() throws ExecutionException, InterruptedException {
        //when
        doReturn(0).when(server).processRequest(any(Request.class));
        client.sendRequests(server);

        //then
        verify(server, times(requestsNumber)).processRequest(any(Request.class));
    }
}
