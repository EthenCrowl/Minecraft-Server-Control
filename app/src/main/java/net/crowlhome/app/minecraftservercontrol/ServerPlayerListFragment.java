package net.crowlhome.app.minecraftservercontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ethen on 4/30/17.
 * Copyright Ethen Crowl
 */

public class ServerPlayerListFragment extends Fragment
        implements DownloadPlayerUUIDResponse, DownloadPlayerFaceResponse, QueryServerResponse {

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
    private String[] playerNameArray;
    private List<Player> previouslyConnectedPlayerObjects;
    private int numPlayersToDownload;
    private int numPlayersDownloaded;
    private PlayerAdapter mPlayerAdapter;
    private ListView list;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        server_id = getArguments().getInt("SERVER_ID");
        db = new DatabaseHandler(getActivity());
        getServerObject(server_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player_server_list,
                container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.player_list_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryServer();
            }
        });

        initialSetup();
    }

    private void initialSetup() {
        createPlayerNameList();
        getPreviousPlayerObjects(server_id);
        if (previouslyConnectedPlayerObjects.size() > 0) {
            if (!allPlayerObjectsGood()) {
                List<String> nameList = new ArrayList<>();
                for (Player player : previouslyConnectedPlayerObjects) {
                    nameList.add(player.get_name());
                }
                downloadPlayerInformation(nameList);
            }
        }
        checkForNewPlayers(playerNameArray, previouslyConnectedPlayerObjects);
        createPlayerListView();
    }

    private void refreshEverything() {
        createPlayerNameList();
        getPreviousPlayerObjects(server_id);
        if (previouslyConnectedPlayerObjects.size() > 0) {
            if (!allPlayerObjectsGood()) {
                List<String> nameList = new ArrayList<>();
                for (Player player : previouslyConnectedPlayerObjects) {
                    nameList.add(player.get_name());
                }
                downloadPlayerInformation(nameList);
            }
        }
        checkForNewPlayers(playerNameArray, previouslyConnectedPlayerObjects);
        refreshPlayerListView();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getServerObject(int server_id) {
        currentServer = db.getServer(server_id);
    }

    private void createPlayerNameList() {
        String rawPlayerNameList = currentServer.get_currentPlayerNames();
        playerNameArray = rawPlayerNameList.split(", ");
    }

    private void getPreviousPlayerObjects(int server_id) {
        previouslyConnectedPlayerObjects = db.getAllPlayers(server_id);
    }

    private void checkForNewPlayers(String[] currentPlayerNames,
                                    List<Player> previousPlayerObjects) {
        List<String> playersToAdd = new ArrayList<>();

        if (currentPlayerNames.length > 0 && !currentPlayerNames[0].equals("")) {
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
        }

        if (playersToAdd.size() > 0) {
            downloadPlayerInformation(playersToAdd);
        } else {
            checkIfOnline();
        }
    }

    private void downloadPlayerInformation(List<String> playerNameList) {
        for (String playerName : playerNameList) {
            numPlayersToDownload = numPlayersToDownload + 1;
            Player player = new Player(server_id, playerName);
            DownloadPlayerUUID downloadPlayerUUID = new DownloadPlayerUUID();
            downloadPlayerUUID.delegate = this;
            downloadPlayerUUID.execute(player);
        }
    }

    private void checkIfOnline() {
        for (Player player : previouslyConnectedPlayerObjects) {
            player.set_is_online(0);
            if (playerNameArray.length > 0 && !playerNameArray[0].equals("")) {
                for (String name : playerNameArray) {
                    if (name.equals(player.get_name())) {
                        player.set_is_online(1);
                        db.updatePlayer(player);
                        break;
                    }
                }
                db.updatePlayer(player);
            } else {
                db.updatePlayer(player);
            }
        }
        getPreviousPlayerObjects(server_id);
    }

    private void createPlayerListView() {
        mPlayerAdapter = new PlayerAdapter(getActivity(), (ArrayList<Player>)
                previouslyConnectedPlayerObjects);
        list = (ListView) getView().findViewById(R.id.player_list_view);
        list.setAdapter(mPlayerAdapter);
    }

    private void refreshPlayerListView() {
        getPreviousPlayerObjects(server_id);
        mPlayerAdapter.clear();
        mPlayerAdapter.addAll(previouslyConnectedPlayerObjects);
        mPlayerAdapter.notifyDataSetChanged();
    }

    private void queryServer() {
        QueryServer queryServer = new QueryServer();
        queryServer.delegate = this;
        queryServer.execute(currentServer);
    }

    private boolean allPlayerObjectsGood() {
        for (Player player : previouslyConnectedPlayerObjects) {
            if (player.get_uuid() == null) {
                return false;
            }
            if (!player.hasFace()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void downloadPlayerUUIDProcessFinish(Player result) {
        DownloadPlayerFace downloadPlayerFace = new DownloadPlayerFace();
        downloadPlayerFace.delegate = this;
        downloadPlayerFace.execute(result);
    }

    @Override
    public void downloadPlayerFaceProcessFinish(Player result) {
        numPlayersDownloaded = numPlayersDownloaded + 1;
        db.addPlayer(result);
        if (numPlayersDownloaded == numPlayersToDownload) {
            numPlayersDownloaded = 0;
            numPlayersToDownload = 0;
            getPreviousPlayerObjects(server_id);
            createPlayerNameList();
            checkIfOnline();
        }
    }

    @Override
    public void queryProcessFinish(Server output) {
        db.updateServer(output);
        currentServer = db.getServer(server_id);
        refreshEverything();
    }

}
