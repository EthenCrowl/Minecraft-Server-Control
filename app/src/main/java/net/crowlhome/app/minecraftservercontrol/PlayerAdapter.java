package net.crowlhome.app.minecraftservercontrol;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ethen on 3/5/17.
 * Copyright Ethen Crowl
 */

public class PlayerAdapter extends ArrayAdapter<Player> {

    private String currentConnectedPlayers;

    public PlayerAdapter(Context context, ArrayList<Player> players, String _currentConnectedPlayers) {
        super(context, 0, players);
        currentConnectedPlayers = _currentConnectedPlayers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Player player = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.player_list_item, parent, false);
        }



        if (face_bytes != null) {
            Bitmap player_icon_image = BitmapFactory.decodeByteArray(face_bytes, 0, face_bytes.length);
            Bitmap resized = Bitmap.createScaledBitmap(player_icon_image, 96, 96, true);
            player_face.setImageBitmap(resized);

        }


        return convertView;
    }
}
