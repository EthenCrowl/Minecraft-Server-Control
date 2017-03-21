package net.crowlhome.app.minecraftservercontrol;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * Created by ethen on 3/5/17.
 * Copyright Ethen Crowl
 */

public class PlayerAdapter extends ArrayAdapter<Player> {

    private List<String> playerList;
    private DatabaseHandler db;
    private Server currentServer;
    private int server_id;

    public PlayerAdapter(Context context, ArrayList<Player> allPlayers, int _server_id) {
        super(context, 0, allPlayers);
        server_id = _server_id;
        db = new DatabaseHandler(context);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        currentServer = db.getServer(server_id);
        try {
            String currentPlayers = currentServer.get_currentPlayerNames();
            String[] reSplit = currentPlayers.split(", ");
            playerList = Arrays.asList(reSplit);
        } catch (Exception e) {
            e.printStackTrace();
            playerList = new ArrayList<>();
        }
        Player player = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.player_list_item, parent, false);
        }

        byte[] face_bytes = player.get_face();
        ImageView player_face_view = (ImageView) convertView.findViewById(R.id.player_face_view);
        TextView player_name_view = (TextView) convertView.findViewById(R.id.player_name_view);
        player_name_view.setText(player.get_name());

        if (face_bytes != null) {
            Bitmap player_icon_image = BitmapFactory.decodeByteArray(face_bytes, 0, face_bytes.length);
            Bitmap resized = Bitmap.createScaledBitmap(player_icon_image, 96, 96, true);
            player_face_view.setImageBitmap(resized);

            Boolean playerOnline = false;
            for (String name : playerList) {
                if (Objects.equals(player.get_name(), name)) {
                    playerOnline = true;
                }
            }

            if (!playerOnline) {
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);

                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                player_face_view.setColorFilter(filter);
            }
        }


        return convertView;
    }
}
