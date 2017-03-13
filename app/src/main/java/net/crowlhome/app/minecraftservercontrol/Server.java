package net.crowlhome.app.minecraftservercontrol;

/**
 * Created by ethen on 2/26/17.
 * Copyright Ethen Crowl
 */

public class Server {

    // private variables
    private int _id;
    private String _serverName;
    private String _serverAddress;
    private int _serverPort;
    private int _queryPort;
    private int _rconPort;
    private String _rconPass;
    private int _connectedPlayers;
    private int _maxPlayers;
    private String _currentPlayerNames;

    // Empty Constructor
    public Server() {

    }

    // constructor
    public Server(String serverName, String serverAddress, int serverPort, int queryPort, int rconPort, String rconPass) {
        this._serverName = serverName;
        this._serverAddress = serverAddress;
        this._serverPort = serverPort;
        this._queryPort = queryPort;
        this._rconPort = rconPort;
        this._rconPass = rconPass;
    }

    public Server(int _id, String _serverName, String _serverAddress, int serverPort, int _queryPort, int _rconPort, String _rconPass, int _connectedPlayers, int _maxPlayers, String _currentPlayerNames) {
        this._id = _id;
        this._serverName = _serverName;
        this._serverAddress = _serverAddress;
        this._serverPort = serverPort;
        this._queryPort = _queryPort;
        this._rconPort = _rconPort;
        this._rconPass = _rconPass;
        this._connectedPlayers = _connectedPlayers;
        this._maxPlayers = _maxPlayers;
        this._currentPlayerNames = _currentPlayerNames;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_serverName() {
        return _serverName;
    }

    public void set_serverName(String _serverName) {
        this._serverName = _serverName;
    }

    public String get_serverAddress() {
        return _serverAddress;
    }

    public void set_serverAddress(String _serverAddress) {
        this._serverAddress = _serverAddress;
    }

    public int get_serverPort() {
        return _serverPort;
    }

    public void set_serverPort(int _serverPort) {
        this._serverPort = _serverPort;
    }

    public int get_rconPort() {
        return _rconPort;
    }

    public void set_rconPort(int _rconPort) {
        this._rconPort = _rconPort;
    }

    public int get_queryPort() {
        return _queryPort;
    }

    public void set_queryPort(int _queryPort) {
        this._queryPort = _queryPort;
    }

    public String get_rconPass() {
        return _rconPass;
    }

    public void set_rconPass(String _rconPass) {
        this._rconPass = _rconPass;
    }

    public int get_connectedPlayers() {
        return _connectedPlayers;
    }

    public void set_connectedPlayers(int _connectedPlayers) {
        this._connectedPlayers = _connectedPlayers;
    }

    public int get_maxPlayers() {
        return _maxPlayers;
    }

    public void set_maxPlayers(int _maxPlayers) {
        this._maxPlayers = _maxPlayers;
    }

    public String get_currentPlayerNames() {
        return _currentPlayerNames;
    }

    public void set_currentPlayerNames(String _currentPlayerNames) {
        this._currentPlayerNames = _currentPlayerNames;
    }

}
