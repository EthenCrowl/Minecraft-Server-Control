package net.crowlhome.app.minecraftservercontrol;

import android.content.Context;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        TextView address = (TextView) convertView.findViewById(R.id.server_item_address);
        TextView current_players = (TextView) convertView.findViewById(R.id.server_item_current_players);
        TextView max_players = (TextView) convertView.findViewById(R.id.server_item_max_players);
        name.setText(server.get_serverName());
        address.setText(server.get_serverAddress());
        current_players.setText(Integer.toString(server.get_connectedPlayers()));
        max_players.setText(Integer.toString(server.get_maxPlayers()));

        return convertView;
    }
}
