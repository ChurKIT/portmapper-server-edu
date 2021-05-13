package portMapperCommands.service.impl;

import context.Context;
import portMapperCommands.service.CommandService;
import service.impl.SocketServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class CommandServiceImpl implements CommandService {

    private SocketServiceImpl socketService = SocketServiceImpl.getInstance();

    @Override
    public List<Context> getAllTransmitterHistory() {
        return socketService.getAllContexts();
    }

    @Override
    public List<String> getAllActiveClientsAddresses() {
        List<String> result = new ArrayList<>();
        List<Context> contextList = socketService.getAllActiveContexts();
        for (Context context : contextList){
            result.add(context.getClientAddress());
        }
        return result;
    }

}
