package portMapperCommands;

import context.ClientInfo;
import context.Message;
import portMapperCommands.service.impl.CommandServiceImpl;

import java.util.List;
import java.util.Scanner;

public class Command {

    private CommandServiceImpl commandService = new CommandServiceImpl();
    private Scanner scanner = new Scanner(System.in);

    public Command() {
    }

    public void waitCommand(){
        while (true){
            String command = scanner.next();
            switch (command){
                case "history":
                    List<ClientInfo> contextList = commandService.getAllTransmitterHistory();
                    for (ClientInfo context : contextList){
                        System.out.println(context.toString());
                    }
                    break;
                case "clients":
                    List<String> clients = commandService.getAllActiveClientsAddresses();
                    for (String str : clients){
                        System.out.println(str);
                    }
                    break;
                case "messages":
                    List<Message> messages = commandService.getAllMessages();
                    for (Message message : messages){
                        System.out.println(message.toString());
                    }
                    break;
            }
        }
    }
}
