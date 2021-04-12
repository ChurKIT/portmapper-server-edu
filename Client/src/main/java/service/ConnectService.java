package service;

public interface ConnectService {

    public boolean connect();

    public boolean mapTo(String uuid);

    public void query(String query);

}
