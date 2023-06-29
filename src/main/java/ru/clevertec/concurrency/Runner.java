package ru.clevertec.concurrency;

import ru.clevertec.concurrency.model.Client;
import ru.clevertec.concurrency.model.Server;

public class Runner {
    public static void main(String[] args) throws Exception {
        Client client = new Client(100);
        Server server = new Server();

        client.sendRequests(server);
    }
}
