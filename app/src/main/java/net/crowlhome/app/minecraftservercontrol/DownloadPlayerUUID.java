package net.crowlhome.app.minecraftservercontrol;

import android.os.AsyncTask;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ethen on 3/13/17.
 * Copyright Ethen Crowl
 */

public class DownloadPlayerUUID extends AsyncTask<Player, Void, Player> {
    public DownloadPlayerUUIDResponse delegate = null;

    private Player input;
    private Player output;

    protected Player doInBackground(Player... players) {
        input = players[0];

        String username = input.get_name();
        String urls = "https://api.mojang.com/users/profiles/minecraft/" + username;
        try {
            URL url = new URL(urls);
            try {
                // create new Player object with new values
                output = new Player();
                output.set_server_id(input.get_server_id());
                output.set_name(input.get_name());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream inputStream = conn.getInputStream();

                InputStreamReader inputStreamReader = null;
                JsonReader jsonReader = null;
                String uuid;
                try {
                    inputStreamReader = new InputStreamReader(inputStream);
                    jsonReader = new JsonReader(inputStreamReader);
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        String valueName = jsonReader.nextName();
                        Boolean isValueNameNull = jsonReader.peek() == JsonToken.NULL;
                        if (valueName.equals("id") && !isValueNameNull) {
                            uuid = jsonReader.nextString();
                            output.set_uuid(uuid);
                        }
                    }
                } catch (Exception except) {
                    except.printStackTrace();
                }
            } catch (Exception except) {
                except.printStackTrace();
            }
        } catch (Exception except) {
            except.printStackTrace();
        }
        return output;
    }

    protected void onPostExecute(Player result) {
        delegate.downloadPlayerUUIDProcessFinish(output);
    }

}