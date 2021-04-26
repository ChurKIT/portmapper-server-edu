import service.ConnectService;
import service.impl.ConnectServiceImpl;

import java.io.IOException;

public class Run {



    public static void main(String[] args) throws IOException {
        ConnectService service = new ConnectServiceImpl();
            service.connect();
            service.mapTo();
//            while (!service.isDone()) {
                service.query();
                System.out.println("Response:");
                System.out.println(service.readResponse());
//            }
            service.close();
//        Socket toTargetServer = new Socket("localhost", 9090);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(toTargetServer.getInputStream()));
//        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(toTargetServer.getOutputStream()));
//        writer.write("GET /" + " " + " HTTP/1.1\r\n" +
//                "Host: " + "localhost" + "\r\n\r\n");
//        writer.flush();
//        toTargetServer.shutdownOutput();
//        StringBuilder response = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null){
//            response.append(line);
//        }
//        System.out.println(response);
//        reader.close();
//        toTargetServer.close();
    }

}
