package net.crowlhome.app.minecraftservercontrol;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
        weather_buttons();
        time_buttons();
    }

    private void sendRcon(String commandString, int mode) {
        List<Command> commandArrayList = new ArrayList<>();
        Command command = new Command(commandString, currentServer.get_serverAddress(),
                currentServer.get_rconPort(), currentServer.get_rconPass(), mode);
        commandArrayList.add(command);

        SendCommand sendCommand = new SendCommand();
        sendCommand.delegate = this;
        sendCommand.execute(commandArrayList);
    }

    private void getServerObject(int server_id) {
        currentServer = db.getServer(server_id);
    }

    private void weather_buttons() {
        // Clear
        final Button weather_clear = (Button) getView().findViewById(R.id.weather_clear);
        weather_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRcon("weather clear", 0);
            }
        });
        // Rain
        final Button weather_rain = (Button) getView().findViewById(R.id.weather_rain);
        weather_rain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRcon("weather rain", 0);
            }
        });
        // Rain and Thunder
        final Button weather_thunder = (Button) getView().findViewById(R.id.weather_thunder);
        weather_thunder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRcon("weather thunder", 0);
            }
        });
    }

    private void time_buttons() {
        // Day
        final Button time_dawn = (Button) getView().findViewById(R.id.time_dawn);
        time_dawn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRcon("time query daytime", 1);
            }
        });

        // Noon
        final Button time_noon = (Button) getView().findViewById(R.id.time_noon);
        time_noon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRcon("time query daytime", 2);
            }
        });

        // Dusk
        final Button time_dusk = (Button) getView().findViewById(R.id.time_dusk);
        time_dusk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRcon("time query daytime", 3);
            }
        });

        // Midnight
        final Button time_midnight = (Button) getView().findViewById(R.id.time_midnight);
        time_midnight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRcon("time query daytime", 4);
            }
        });
    }

    @Override
    public void sendCommandProcessFinish(Command result) {
        if (!result.getOutput().equals("Error Reaching Server")
                & !result.getOutput().equals("Authentication Error")) {

            String currentTimeString;
            int currentTime;
            int timeToAdd;
            String timeToAddString;
            String commandString;

            switch (result.getMode()) {
                case 0:
                    Snackbar.make(getView().findViewById(
                            R.id.server_buttons_root),
                            result.getOutput(),
                            Snackbar.LENGTH_LONG).show();
                    break;
                case 1:
                    currentTimeString = result.getOutput();
                    currentTimeString = currentTimeString.substring(8);
                    currentTime = Integer.parseInt(currentTimeString);
                    timeToAdd = (24000 - currentTime);
                    timeToAddString = Integer.toString(timeToAdd);
                    commandString = "time add " + timeToAddString;
                    sendRcon(commandString, 5);
                    break;
                case 2:
                    currentTimeString = result.getOutput();
                    currentTimeString = currentTimeString.substring(8);
                    currentTime = Integer.parseInt(currentTimeString);
                    timeToAdd = (24000 - currentTime) + 6000;
                    timeToAddString = Integer.toString(timeToAdd);
                    commandString = "time add " + timeToAddString;
                    sendRcon(commandString, 6);
                    break;
                case 3:
                    currentTimeString = result.getOutput();
                    currentTimeString = currentTimeString.substring(8);
                    currentTime = Integer.parseInt(currentTimeString);
                    timeToAdd = (24000 - currentTime) + 12000;
                    timeToAddString = Integer.toString(timeToAdd);
                    commandString = "time add " + timeToAddString;
                    sendRcon(commandString, 7);
                    break;
                case 4:
                    currentTimeString = result.getOutput();
                    currentTimeString = currentTimeString.substring(8);
                    currentTime = Integer.parseInt(currentTimeString);
                    timeToAdd = (24000 - currentTime) + 18000;
                    timeToAddString = Integer.toString(timeToAdd);
                    commandString = "time add " + timeToAddString;
                    sendRcon(commandString, 8);
                    break;
                case 5:
                    Snackbar.make(getView().findViewById(
                            R.id.server_buttons_root),
                            "Set the time to Dawn",
                            Snackbar.LENGTH_LONG).show();
                    break;
                case 6:
                    Snackbar.make(getView().findViewById(
                            R.id.server_buttons_root),
                            "Set the time to Noon",
                            Snackbar.LENGTH_LONG).show();
                    break;
                case 7:
                    Snackbar.make(getView().findViewById(
                            R.id.server_buttons_root),
                            "Set the time to Dusk",
                            Snackbar.LENGTH_LONG).show();
                    break;
                case 8:
                    Snackbar.make(getView().findViewById(
                            R.id.server_buttons_root),
                            "Set the time to Midnight",
                            Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
