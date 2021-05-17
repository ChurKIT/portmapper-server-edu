package portMapperCommands.service.impl;

import context.ClientInfo;
import context.Message;
import context.dbservice.impl.ClientInfoDAOImpl;
import portMapperCommands.service.CommandService;

import java.util.ArrayList;
import java.util.List;

public class CommandServiceImpl implements CommandService {

//    private SocketServiceImpl socketService = SocketServiceImpl.getInstance();
    ClientInfoDAOImpl clientInfoDAO = new ClientInfoDAOImpl();

    @Override
    public List<ClientInfo> getAllTransmitterHistory() {
        return clientInfoDAO.getAllClientInfo();
    }

    @Override
    public List<String> getAllActiveClientsAddresses() {
        List<String> result = new ArrayList<>();
        List<ClientInfo> contextList = clientInfoDAO.getAllClientInfo();
        for (ClientInfo context : contextList){
            result.add(context.getClientAddress());
        }
        return result;
    }

    @Override
    public List<Message> getAllMessages() {
        List<Message> result = new ArrayList<>();
        List<ClientInfo> clientInfoList = clientInfoDAO.getAllClientInfo();
        for (ClientInfo clientInfo : clientInfoList){
            result.add(clientInfo.getContext().getMessage());
        }
        return result;
    }
}
