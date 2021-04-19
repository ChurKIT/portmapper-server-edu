package service;

public interface ConnectService {

    void connect();

    void query();

    void mapTo();

    String readResponse();

//    void sendRequest();

    void close();

}
