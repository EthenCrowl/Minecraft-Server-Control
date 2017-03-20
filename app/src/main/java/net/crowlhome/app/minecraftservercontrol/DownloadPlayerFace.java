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

    private Server output;
    private byte[] outputByteArray;

    protected Player doInBackground(Player... players) {
        String uuid = players[0].get_uuid();
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
                Player result = players[0];
                result.set_face(outputByteArray);

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
        delegate.downloadPlayerFaceProcessFinish(result);
    }

}