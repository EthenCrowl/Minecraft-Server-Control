package net.crowlhome.app.minecraftservercontrol;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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

    public PlayerAdapter(Context context, ArrayList<Player> allPlayers) {
        super(context, 0, allPlayers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

            if (player.get_is_online() == 0) {
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);

                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                player_face_view.setColorFilter(filter);
            }

            if (player.get_is_online() == 1) {
                ColorMatrix matrix = new ColorMatrix();

                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                player_face_view.setColorFilter(filter);
            }
        }


        return convertView;
    }
}
