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

public class DownloadPlayerFace extends AsyncTask<Player, Void, Player> {
    public DownloadPlayerFaceResponse delegate = null;

    private Player input;
    private Player output;
    private byte[] outputByteArray;

    protected Player doInBackground(Player... players) {
        input = players[0];

        String uuid = input.get_uuid();
        String urls = "https://crafatar.com/avatars/" + uuid + "?overlay";
        try {
            URL url = new URL(urls);
            try
            {
                HttpURLConnection conn= (HttpURLConnection)url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                outputByteArray = IOUtils.toByteArray(inputStream);

                // create new Player object with new values
                output = new Player();
                output.set_face(outputByteArray);
                output.set_server_id(input.get_server_id());
                output.set_name(input.get_name());
                output.set_uuid(input.get_uuid());

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

    protected void onPostExecute(Player result) {
        delegate.downloadPlayerFaceProcessFinish(output);
    }

}