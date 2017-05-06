package net.crowlhome.app.minecraftservercontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ethen on 5/5/17.
 * Copyright Ethen Crowl
 */

public class ServerCommandButtonsFragment extends Fragment implements SendCommandResponse {
    int server_id;
    DatabaseHandler db;
    Server currentServer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        server_id = getArguments().getInt("SERVER_ID");
        db = new DatabaseHandler(getActivity());
        getServerObject(server_id);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_server_buttons, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final Button weather_clear = (Button) getView().findViewById(R.id.weather_clear);
        weather_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRcon("weather clear");
            }
        });

        final Button weather_rain = (Button) getView().findViewById(R.id.weather_rain);
        weather_rain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRcon("weather rain");
            }
        });

        final Button weather_thunder = (Button) getView().findViewById(R.id.weather_thunder);
        weather_thunder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRcon("weather thunder");
            }
        });
    }

    private void sendRcon(String commandString) {
        List<Command> commandArrayList = new ArrayList<>();
        Command command = new Command(commandString, currentServer.get_serverAddress(),
                currentServer.get_rconPort(), currentServer.get_rconPass());
        commandArrayList.add(command);

        SendCommand sendCommand = new SendCommand();
        sendCommand.delegate = this;
        sendCommand.execute(commandArrayList);
    }

    private void getServerObject(int server_id) {
        currentServer = db.getServer(server_id);
    }

    @Override
    public void sendCommandProcessFinish(String result) {

    }
}
