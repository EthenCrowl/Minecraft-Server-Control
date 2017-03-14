package net.crowlhome.app.minecraftservercontrol;

import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by ethen on 3/13/17.
 * Copyright Ethen Crowl
 */

public class DownloadServerImage extends AsyncTask<Server, Void, Server> {
    public DownloadServerImageResponse delegate = null;

    private Server output;
    private byte[] outputByteArray;

    protected Server doInBackground(Server... servers) {
        String urlBase = servers[0].get_serverAddress();
        String urlPort = Integer.toString(servers[0].get_serverPort());
        String urls = "http://mcapi.ca/query/" + urlBase + ":" + urlPort + "/icon";
        try {
            URL url = new URL(urls);
            try
            {
                HttpURLConnection conn= (HttpURLConnection)url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                outputByteArray = IOUtils.toByteArray(inputStream);

                // create new Server object with new values
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

                // Create the output server object with the new values
                output = new Server(output_id, output_name, output_address, output_server_port,
                        output_query_port, output_rcon_port, output_rcon_pass,
                        output_connected_players, output_max_players,
                        output_current_player_names, output_MOTD, outputByteArray);

                /**
                /  BitmapFactory.Options options = new BitmapFactory.Options();
                /   options.inSampleSize =
                /       bmImg = BitmapFactory.decodeStream(is,null,options);
                /
                /   img.setImageBitmap(bmImg);
                /
                /   dialog.dismiss();
                */
            }
            catch(IOException e)
            {
                e.printStackTrace();
//          Toast.makeText(PhotoRating.this, "Connection Problem. Try Again.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception except) {
            except.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Server result) {
        delegate.downloadServerImageProcessFinish(output);
    }

}