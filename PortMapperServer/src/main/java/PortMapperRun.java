import poolThreads.PoolThreads;
import portMapperCommands.Command;

public class PortMapperRun {


    public static void main(String[] args) {
        SocketTransmitter socketTransmitter = new SocketTransmitter();
//            socketTransmitter.run();
        PoolThreads.INSTANCE.executor.submit(socketTransmitter);
        Command command = new Command();
        command.waitCommand();

    }
}
