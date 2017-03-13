package net.crowlhome.app.minecraftservercontrol;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ethen on 3/12/17.
 * Copyright Ethen Crowl
 */

public class QueryServer extends AsyncTask<Server, Void, Server>{
    public QueryServerResponse delegate = null;

    private Exception exception;
    private int connectedPlayers = 0;
    private String currentPlayerNames;
    private int maxPlayers;
    private String[] onlinePlayers = new String[0];

    protected Server doInBackground(Server... servers) {

        String serverAddress = servers[0].get_serverAddress();
        int queryPort = servers[0].get_queryPort();
        int serverPort = servers[0].get_serverPort();

        InetSocketAddress queryAddress = new InetSocketAddress(serverAddress, queryPort);
        InetSocketAddress address = new InetSocketAddress(serverAddress, serverPort);

        Query query = new Query(queryAddress, address);
        String[] onlinePlayers = new String[1];
        Map<String, String> values = new HashMap<>();


        try {
            query.sendQuery();
            onlinePlayers = query.getOnlineUsernames();
            values = query.getValues();
            if (onlinePlayers.length == 0) {
                onlinePlayers = new String[1];
                onlinePlayers[0] = "Empty";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentPlayerNames = TextUtils.join(",", onlinePlayers);
        connectedPlayers = Integer.parseInt(values.get("numplayers"));
        maxPlayers = Integer.parseInt(values.get("maxplayers"));

        servers[0].set_currentPlayerNames(currentPlayerNames);
        servers[0].set_connectedPlayers(connectedPlayers);
        servers[0].set_maxPlayers(maxPlayers);

        return null;
    }

    @Override
    protected void onPostExecute(Server result) {
        delegate.processFinish(result);
    }

}
