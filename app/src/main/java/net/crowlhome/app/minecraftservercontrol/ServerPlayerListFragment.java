package net.crowlhome.app.minecraftservercontrol;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ethen on 4/30/17.
 * Copyright Ethen Crowl
 */

public class ServerPlayerListFragment extends Fragment{
    /*
    This fragment should load the list of previously connected players from the database.
    Upon loading the fragment, unpack the server id and convert it into a usable value.
    Then, retrieve the server object from the database with the server id.
    Then, create a list of currently connected player names from the server object.
    Then, create a list of previously connected players from the database.
    Then, compare the two lists for any new players.
    If any new players are found.
        Then, retrieve all information for the new players.
        Then, save all new players to the database.
        Clean up variables.
        Pull a new list of all previously connected players from the database in place of the old list.
    Compare that list with the name list from the server object to determine if the players are online.
    Then, pass the completed list to the player adapter.

     */

    private int server_id;
    private DatabaseHandler db;
    private Server currentServer;
    String[] playerNameArray;
    List<Player> previouslyConnectedPlayerObjects;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        server_id = getArguments().getInt("SERVER_ID");
        db = new DatabaseHandler(getActivity());
        getServerObject(server_id);


        return inflater.inflate(R.layout.fragment_player_server_list,
                container, false);
    }

    private void getServerObject(int server_id) {
        currentServer = db.getServer(server_id);
    }

    private void createPlayerNameList() {
        String rawPlayerNameList = currentServer.get_currentPlayerNames();
        playerNameArray = rawPlayerNameList.split(",");
    }

    private void getPreviousPlayerObjects(int server_id) {
        previouslyConnectedPlayerObjects = db.getAllPlayers(server_id);
    }

    private void checkForNewPlayers(String[] currentPlayerNames,
                                    List<Player> previousPlayerObjects) {
        List<String> playersToAdd = new ArrayList<>();

        for (String playerName : currentPlayerNames) {
            boolean exists = false;

            for (Player player : previousPlayerObjects) {
                if (playerName.equals(player.get_name())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                playersToAdd.add(playerName);
            }

        }

        if (playersToAdd.size() > 0) {
            // Call the method to download player info.
        }
    }

    private void downloadPlayerInformation(List<String> playerNameList) {

    }

}
