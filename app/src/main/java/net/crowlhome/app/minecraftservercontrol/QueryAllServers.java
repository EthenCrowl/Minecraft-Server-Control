package net.crowlhome.app.minecraftservercontrol;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ethen on 3/12/17.
 * Copyright Ethen Crowl
 */

public class QueryAllServers extends AsyncTask<List<Server>, Void, List<Server>>{
    public QueryAllServersResponse delegate = null;

    private List<Server> output = new ArrayList<Server>();


    protected List<Server> doInBackground(List<Server>... serverList) {

        for (Server server : serverList[0]) {
            // Set the local variables
            int connectedPlayers = 0;
            int maxPlayers = 0;
            String output_MOTD = "";
            int successful_query = 0;
            Server outputServer;

            // Set the network params
            String serverAddress = server.get_serverAddress();
            int queryPort = server.get_queryPort();
            int serverPort = server.get_serverPort();

            // Generate the server addresses
            InetSocketAddress queryAddress = new InetSocketAddress(serverAddress, queryPort);
            InetSocketAddress address = new InetSocketAddress(serverAddress, serverPort);

            // Create the Query object and return values
            Query query = new Query(queryAddress, address);
            String[] onlinePlayers = new String[1];
            Map<String, String> values = new HashMap<>();
            String currentPlayerNames = new String();

            // Query the server for usernames and values
            try {
                query.sendQuery();
                onlinePlayers = query.getOnlineUsernames();
                values = query.getValues();
                if (onlinePlayers.length == 0) {
                    onlinePlayers = new String[0];
                }
                successful_query = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Join the username array into a single comma-separated String
            if (onlinePlayers.length > 0) {
                currentPlayerNames = TextUtils.join(", ", onlinePlayers);
            }
            if (values.size() > 0) {
                connectedPlayers = Integer.parseInt(values.get("numplayers"));
                maxPlayers = Integer.parseInt(values.get("maxplayers"));
                output_MOTD = values.get("hostname");
            }

            // Get the values for the output object
            int output_id = server.get_id();
            String output_name = server.get_serverName();
            String output_address = server.get_serverAddress();
            int output_server_port = server.get_serverPort();
            int output_query_port = server.get_queryPort();
            int output_rcon_port = server.get_rconPort();
            String output_rcon_pass = server.get_rconPass();
            byte[] output_icon = server.get_serverIcon();

            // Create the output server object with the new values
            if (successful_query == 1) {
                outputServer = new Server(output_id, output_name, output_address, output_server_port,
                        output_query_port, output_rcon_port, output_rcon_pass, connectedPlayers, maxPlayers,
                        currentPlayerNames, output_MOTD, output_icon);

                output.add(outputServer);
            }
        }
        return null;
    }

    protected void onPostExecute(List<Server> serverList) {
        delegate.queryAllServersProcessFinish(output);
    }

}
