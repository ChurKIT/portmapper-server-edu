package context.dbservice.impl;

import context.ClientInfo;
import context.Context;
import context.Message;
import context.dbservice.ClientInfoDAO;
import org.apache.log4j.Logger;
import service.impl.SocketServiceImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ClientInfoDAOImpl implements ClientInfoDAO {

    private static final Logger log = Logger.getLogger(ClientInfoDAOImpl.class);

    private SocketServiceImpl socketService = SocketServiceImpl.getInstance();
    private String DB_URL;
    private String DB_DRIVER;
    private String DB_USERNAME;
    private String DB_PASSWORD;

    public ClientInfoDAOImpl() {
        getProperties();
    }

    public void getProperties(){
        try {
            FileInputStream fis = new FileInputStream("PortMapperServer/src/main/resources/portmapper.properties");
            Properties properties = new Properties();
            properties.load(fis);
            DB_URL = properties.getProperty("db.url");
            DB_DRIVER = properties.getProperty("db.driver");
            DB_USERNAME = properties.getProperty("db.username");
            DB_PASSWORD = properties.getProperty("db.password");
        } catch (IOException e){
            log.error("ERROR: Properties file is missing or damaged");
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection(){
        Connection connection = null;
        getProperties();
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            log.error("ERROR: DB connection not established");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            log.error("ERROR: JDBC Driver not found");
            throw new RuntimeException(e);
        }
        return connection;
    }


    @Override
    public void save(ClientInfo clientInfo) {
        String queryMessage = "INSERT INTO Messages (id, request, response) VALUES (?, ?, ?)";
        String queryContext = "INSERT INTO Contexts (id, requestBytes, responseBytes, startSession, stopSession, workTime, message_id)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";
        String queryClient = "INSERT INTO Clients (id, clientAddress, context_id) VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = getConnection().prepareStatement(queryMessage);
            statement.setInt(1, clientInfo.getContext().getId());
            statement.setString(2, clientInfo.getContext().getRequest());
            statement.setString(3, clientInfo.getContext().getResponse());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            log.error("ERROR: Unable to add message to database");
            throw new RuntimeException(throwables);
        }
        try{
            PreparedStatement statement = getConnection().prepareStatement(queryContext);
            statement.setInt(1, clientInfo.getId());
            statement.setInt(2, clientInfo.getContext().getRequestBytes());
            statement.setInt(3, clientInfo.getContext().getResponseBytes());
            statement.setObject(4, new Timestamp(clientInfo.getContext().getStartSession()));
            statement.setObject(5, new Timestamp(clientInfo.getContext().getStopSession()));
            statement.setInt(6, clientInfo.getContext().getWorkTime());
            statement.setInt(7, clientInfo.getContext().getMessage().getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            log.error("ERROR: Unable to add context to database");
            throw new RuntimeException(throwables);
        }
        try {
            PreparedStatement statement = getConnection().prepareStatement(queryClient);
            statement.setInt(1, clientInfo.getId());
            statement.setString(2, clientInfo.getClientAddress());
            statement.setInt(3, clientInfo.getContext().getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            log.error("ERROR: Unable to add client to database");
            throw new RuntimeException(throwables);
        }

    }

    public List<ClientInfo> getAllClientInfo() {
        List<ClientInfo> result = new ArrayList<>();
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet clientInfoSet = statement.executeQuery("SELECT * FROM CLIENTS");
            ResultSet contextSet = statement.executeQuery("SELECT * FROM CONTEXTS");
            ResultSet messageSet = statement.executeQuery("SELECT * FROM MESSAGES");
            while (clientInfoSet.next() && contextSet.next() && messageSet.next()){
                ClientInfo clientInfo = new ClientInfo();
                clientInfo.setId(clientInfoSet.getInt(1));
                clientInfo.setClientAddress(clientInfoSet.getString(2));

                    Context context = new Context();
                    context.setId(contextSet.getInt(1));
                    context.setRequestBytes(contextSet.getInt(2));
                    context.setResponseBytes(contextSet.getInt(3));
                    context.setStartSession(contextSet.getTimestamp(4).getTime());
                    context.setStopSession(contextSet.getTimestamp(5).getTime());
                    context.setWorkTime(contextSet.getInt(6));

                        Message message = new Message();
                        message.setId(messageSet.getInt(1));
                        message.setRequest(messageSet.getString(2));
                        message.setResponse(messageSet.getString(3));
                        context.setMessage(message);

                    clientInfo.setContext(context);

                result.add(clientInfo);
            }
        } catch (SQLException throwables) {
            log.error("ERROR: Unable to get clientInfo from database");
            throw new RuntimeException(throwables);
        }
        return result;
    }

    @Override
    public ClientInfo getClientInfoById(int id) {
        List<ClientInfo> clientInfoList = getAllClientInfo();
        for (ClientInfo clientInfo : clientInfoList){
            if (clientInfo.getId() == id){
                return clientInfo;
            }
        }
        return new ClientInfo();
    }

    @Override
    public void deleteClientInfo(int id) {
        String query = "DELETE FROM Clients WHERE id = " + id;
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("ERROR: Unable to delete data from DB");
        }
    }

    public int lastIndex() {
        return getAllClientInfo().size();
    }
}
