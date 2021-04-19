import service.ConnectService;
import service.impl.ConnectServiceImpl;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class Run {



    public static void main(String[] args) {
        ConnectService service = new ConnectServiceImpl();
        service.connect();
        service.mapTo();
        service.query();
        System.out.println("Response:");
        System.out.println(service.readResponse());
    }

}
