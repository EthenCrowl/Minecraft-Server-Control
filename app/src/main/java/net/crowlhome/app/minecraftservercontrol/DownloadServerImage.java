package net.crowlhome.app.minecraftservercontrol;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Blob;

/**
 * Created by ethen on 3/13/17.
 * Copyright Ethen Crowl
 */

public class DownloadServerImage extends AsyncTask<Server, Void, String> {
    public DownloadServerImageResponse delegate = null;

    private Server output;
    private byte[] outputBytes;

    protected String doInBackground(Server... servers) {
        try {
            String urlBase = servers[0].get_serverAddress();
            String urlPort = Integer.toString(servers[0].get_serverPort());
            String urls = "http://mcapi.ca/query/" + urlBase + ":" + urlPort + "/icon";
            URL url = new URL(urls);
            InputStream is = (InputStream) url.getContent();
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream rawOutput = new ByteArrayOutputStream();
            while ((bytesRead = is.read(buffer)) != -1) {
                rawOutput.write(buffer, 0, bytesRead);
            }
            outputBytes = rawOutput.toByteArray();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Get the values for the output object
        int output_id = servers[0].get_id();
        String output_name = servers[0].get_serverName();
        String output_address = servers[0].get_serverAddress();
        int output_server_port = servers[0].get_serverPort();
        int output_query_port = servers[0].get_queryPort();
        int output_rcon_port = servers[0].get_rconPort();
        String output_rcon_pass = servers[0].get_rconPass();
        String output_MOTD = servers[0].get_serverMOTD();
        int output_connected_players = servers[0].get_connectedPlayers();
        int output_max_players = servers[0].get_maxPlayers();
        String output_current_player_names = servers[0].get_currentPlayerNames();
        byte[] output_icon = outputBytes;

        // Create the output server object with the new values
        output = new Server(output_id, output_name, output_address, output_server_port,
                output_query_port, output_rcon_port, output_rcon_pass, output_connected_players, output_max_players,
                output_current_player_names, output_MOTD, output_icon);

        return null;
    }

    protected void onPostExecute(byte[] result) {
        delegate.downloadServerImageProcessFinish(output);
    }

}