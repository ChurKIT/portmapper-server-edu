package context.dbservice;

import context.ClientInfo;

import java.util.List;

public interface ClientInfoDAO {

void save(ClientInfo clientInfo);

List<ClientInfo> getAllClientInfo();

ClientInfo getClientInfoById(int id);

void deleteClientInfo(int id);


}
