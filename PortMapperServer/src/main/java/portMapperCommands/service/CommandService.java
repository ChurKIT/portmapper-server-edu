package portMapperCommands.service;

import context.ClientInfo;
import context.Message;

import java.util.List;

public interface CommandService {

    List<ClientInfo> getAllTransmitterHistory();

    List<String> getAllActiveClientsAddresses();

    List<Message> getAllMessages();
}
