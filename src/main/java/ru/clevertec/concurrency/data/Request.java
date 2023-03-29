package ru.clevertec.concurrency.data;

/**
 * Request class, which contains int field
 *
 * @author Yuryeu Andrei
 */
public class Request {
    private final int requestBody;

    public Request(int requestBody) {
        this.requestBody = requestBody;
    }

    public int getRequestBody() {
        return requestBody;
    }
}
