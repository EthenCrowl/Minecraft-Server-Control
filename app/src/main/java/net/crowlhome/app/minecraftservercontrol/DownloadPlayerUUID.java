package net.crowlhome.app.minecraftservercontrol;

import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by ethen on 3/13/17.
 * Copyright Ethen Crowl
 */

public class DownloadPlayerUUID extends AsyncTask<Player, Void, Player> {
    public DownloadPlayerUUIDResponse delegate = null;

    protected Player doInBackground(Player... players) {
        String username = players[0].get_name();
        String urls = "https://api.mojang.com/users/profiles/minecraft/" + username;
        try {
            URL url = new URL(urls);
            try
            {
                HttpURLConnection conn= (HttpURLConnection)url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                JSONObject values = new JSONObject(inputStream.toString());
                String uuid = values.getJSONObject("id").toString();

                // create new Player object with new values
                Player result = players[0];
                result.set_uuid(uuid);

            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        } catch (Exception except) {
            except.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Player result) {
        delegate.downloadPlayerUUIDProcessFinish(result);
    }

}