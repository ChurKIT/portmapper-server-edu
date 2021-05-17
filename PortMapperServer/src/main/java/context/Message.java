package context;

import java.util.concurrent.atomic.AtomicInteger;

public class Message {

    public static transient AtomicInteger id_generator = new AtomicInteger(0);

    int id;
    private String request;
    private String response;

    public Message() {
        this.id = id_generator.addAndGet(1);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", request='" + request + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
