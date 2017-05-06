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

    private String currentPlayerNames = new String();
    private Server output;

    protected Server doInBackground(Server... servers) {

        // Set the network params
        String serverAddress = servers[0].get_serverAddress();
        int queryPort = servers[0].get_queryPort();
        int serverPort = servers[0].get_serverPort();

        // Generate the server addresses
        InetSocketAddress queryAddress = new InetSocketAddress(serverAddress, queryPort);
        InetSocketAddress address = new InetSocketAddress(serverAddress, serverPort);

        // Create the Query object and return values
        Query query = new Query(queryAddress, address);
        String[] onlinePlayers = new String[1];
        Map<String, String> values = new HashMap<>();

        // Query the server for usernames and values
        try {
            query.sendQuery();
            onlinePlayers = query.getOnlineUsernames();
            values = query.getValues();
            if (onlinePlayers.length == 0) {
                onlinePlayers = new String[0];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Join the username array into a single comma-separated String
        if (onlinePlayers.length > 0) {
            currentPlayerNames = TextUtils.join(", ", onlinePlayers);
        }
        int connectedPlayers = Integer.parseInt(values.get("numplayers"));
        int maxPlayers = Integer.parseInt(values.get("maxplayers"));
        String output_MOTD = values.get("hostname");

        /* Get the values for the output object
        int output_id = servers[0].get_id();
        String output_name = servers[0].get_serverName();
        String output_address = servers[0].get_serverAddress();
        int output_server_port = servers[0].get_serverPort();
        int output_query_port = servers[0].get_queryPort();
        int output_rcon_port = servers[0].get_rconPort();
        String output_rcon_pass = servers[0].get_rconPass();
        byte[] output_icon = servers[0].get_serverIcon();

        // Create the output server object with the new values
        output = new Server(output_id, output_name, output_address, output_server_port,
                output_query_port, output_rcon_port, output_rcon_pass, connectedPlayers, maxPlayers,
                currentPlayerNames, output_MOTD, output_icon);
        */

        output = servers[0];
        output.set_connectedPlayers(connectedPlayers);
        output.set_maxPlayers(maxPlayers);
        output.set_currentPlayerNames(currentPlayerNames);
        output.set_serverMOTD(output_MOTD);


        return null;
    }

    protected void onPostExecute(Server server) {
        delegate.queryProcessFinish(output);
    }

}
