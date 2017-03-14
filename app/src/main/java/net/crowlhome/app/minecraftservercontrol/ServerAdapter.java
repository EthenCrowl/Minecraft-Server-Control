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

public class ServerAdapter extends ArrayAdapter<Server> {
    public ServerAdapter (Context context, ArrayList<Server> servers) {
        super(context, 0, servers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Server server = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.server_list_item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.server_item_name);
        TextView motd = (TextView) convertView.findViewById(R.id.server_item_motd);
        TextView num_players = (TextView) convertView.findViewById(R.id.server_item_num_players);
        ImageView server_icon = (ImageView) convertView.findViewById(R.id.server_item_icon);
        name.setText(server.get_serverName());

        String motdTextRaw = server.get_serverMOTD();
        String[] invalidValues = { "�0", "�1", "�2", "�3", "�4", "�5", "�6", "�7", "�8", "�9", "�a", "�b", "�c", "�d", "�e", "�f", "�k", "�l", "�m", "�n", "�o", "�r"};
        String motdText = motdTextRaw;
        for (String str : invalidValues) {
            motdText = motdText.replace(str, "");
        }

        motd.setText(motdText);
        String num_players_string = Integer.toString(server.get_connectedPlayers()) + "/" + Integer.toString(server.get_maxPlayers());
        num_players.setText(num_players_string);
        byte[] icon_bytes = server.get_serverIcon();

        if (icon_bytes != null) {
            Bitmap server_icon_image = BitmapFactory.decodeByteArray(icon_bytes, 0, icon_bytes.length);
            Bitmap resized = Bitmap.createScaledBitmap(server_icon_image, 96, 96, true);
            server_icon.setImageBitmap(resized);

        }


        return convertView;
    }

}
