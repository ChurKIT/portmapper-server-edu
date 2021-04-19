import service.ConnectService;
import service.impl.ConnectServiceImpl;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class Run {



    public static void main(String[] args) {
        ConnectService service = new ConnectServiceImpl();
        try {
            service.connect();
            service.mapTo();
            service.query();
            Thread.sleep(5000);
            System.out.println("Response:");
            System.out.println(service.readResponse());
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }

}
