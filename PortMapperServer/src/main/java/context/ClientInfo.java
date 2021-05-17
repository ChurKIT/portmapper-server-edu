package context;

import java.util.concurrent.atomic.AtomicInteger;

public class ClientInfo {

    public static AtomicInteger id_generator = new AtomicInteger(0);

    private int id;
    private String clientAddress;
    private Context context;

    public ClientInfo() {
        id = id_generator.addAndGet(1);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", clientAddress='" + clientAddress + '\'' +
                ", context=" + context +
                '}';
    }
}
