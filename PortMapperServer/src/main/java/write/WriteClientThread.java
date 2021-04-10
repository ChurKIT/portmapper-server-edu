package write;

import java.io.*;
import java.net.Socket;

public class WriteClientThread extends Thread {

    private Socket socket;
    private BufferedWriter writer;

    public WriteClientThread(Socket socket) throws IOException {
        this.socket = socket;
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {

    }
}
