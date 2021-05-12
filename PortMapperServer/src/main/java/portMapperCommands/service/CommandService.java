package portMapperCommands.service;

import context.Context;

import java.net.InetAddress;
import java.util.List;

public interface CommandService {

    List<Context> getAllTransmitterHistory();

    List<InetAddress> getAllActiveClientsAddresses();

}
