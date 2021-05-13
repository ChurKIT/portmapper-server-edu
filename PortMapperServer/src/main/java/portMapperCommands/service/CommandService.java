package portMapperCommands.service;

import context.Context;

import java.util.List;

public interface CommandService {

    List<Context> getAllTransmitterHistory();

    List<String> getAllActiveClientsAddresses();


}
