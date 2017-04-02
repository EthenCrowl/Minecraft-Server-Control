package net.crowlhome.app.minecraftservercontrol;


/**
 * Created by ethen on 3/19/17.
 * Copyright Ethen Crowl
 */

public class Player {

    // Private variables for a player object
    private int _server_id;
    private String _uuid;
    private String _name;
    private byte[] _face;
    private String _scoreboard;
    private int _is_online;

    public Player() {

    }

    public Player(int server_id, String name) {
        this._server_id = server_id;
        this._name = name;
    }

    public Player(int server_id, String uuid, String name, byte[] face) {
        this._server_id = server_id;
        this._uuid = uuid;
        this._name = name;
        this._face = face;
    }

    public Player(int server_id, String uuid, String name, byte[] face, String scoreboard, int is_online) {
        this._server_id = server_id;
        this._uuid = uuid;
        this._name = name;
        this._face = face;
        this._scoreboard = scoreboard;
        this._is_online = is_online;
    }

    public boolean hasFace() {
        if (_face != null) {
            return true;
        } else {
            return false;
        }
    }

    public int get_server_id() {
        return _server_id;
    }

    public void set_server_id(int _server_id) {
        this._server_id = _server_id;
    }

    public String get_uuid() {
        return _uuid;
    }

    public void set_uuid(String _uuid) {
        this._uuid = _uuid;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public byte[] get_face() {
        return _face;
    }

    public void set_face(byte[] _face) {
        this._face = _face;
    }

    public String get_scoreboard() {
        return _scoreboard;
    }

    public void set_scoreboard(String _scoreboard) {
        this._scoreboard = _scoreboard;
    }

    public int get_is_online() {
        return _is_online;
    }

    public void set_is_online(int _is_online) {
        this._is_online = _is_online;
    }
}
