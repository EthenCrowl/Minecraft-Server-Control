package net.crowlhome.app.minecraftservercontrol;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerActivity extends AppCompatActivity implements DownloadPlayerFaceResponse,
        DownloadPlayerUUIDResponse, QueryServerResponse{

    private int server_id;
    private DatabaseHandler db;
    private PlayerAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Player> playerList = new ArrayList<>();
    private List<Player> allPlayers;
    private int getPlayerFaceCount = 0;
    private int numAllPlayers;
    private int getGetPlayerFaceCountFinished = 0;
    private List<String> currentConnectedPlayers;
    private Server currentServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        db = new DatabaseHandler(this);

        getServerInfo();

        // create a player list if anyone is connected
        if (currentServer.get_connectedPlayers() > 0) {
            updateCurrentPlayerList(currentServer);
        }

        setCurrentPlayersOnline();
        numAllPlayers = allPlayers.size();

        for (Player player : allPlayers) {
            if (!player.hasFace()) {
                getPlayerFaceCount = getPlayerFaceCount + 1;
                getPlayerFace(player);
            }
        }

        createSwipeRefreshLayout();
        createPlayerListView();
    }

    private void setCurrentPlayersOnline() {
        updateCurrentPlayerList(currentServer);
        updateAllPlayers();
        for (Player player : allPlayers) {
            player.set_is_online(0);
            String playerName = player.get_name();
            if (currentConnectedPlayers.contains(playerName)) {
                player.set_is_online(1);
            }
            db.updatePlayer(player);
        }
        updateAllPlayers();
    }

    private void updateAllPlayers() {
        allPlayers = db.getAllPlayers(server_id);
    }

    private void getServerIdFromIntent() {
        String server_id_string = getIntent().getStringExtra("SERVER_ID");
        server_id = Integer.parseInt(server_id_string);
    }

    private void getServerInfo() {
        getServerIdFromIntent();
        currentServer = db.getServer(server_id);
        queryTheServer(currentServer);
    }

    private void createSwipeRefreshLayout() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_server);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryTheServer(currentServer);
            }
        });
    }

    private void refreshPlayerListView() {
        updateAllPlayers();
        mAdapter.clear();
        mAdapter.addAll(allPlayers);
        mAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void createPlayerListView() {
        mAdapter = new PlayerAdapter(this, (ArrayList<Player>) allPlayers);
        ListView list = (ListView) findViewById(R.id.player_list_view);
        list.setAdapter(mAdapter);
    }

    private void updateCurrentPlayerList(Server server) {
        try {
            String currentPlayers = server.get_currentPlayerNames();
            String[] reSplit = currentPlayers.split(", ");
            currentConnectedPlayers = Arrays.asList(reSplit);
            updatePlayersInDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            currentConnectedPlayers = new ArrayList<>();
        }
    }

    private void updatePlayersInDatabase() {
        if (currentConnectedPlayers != null) {
            for (String name : currentConnectedPlayers) {
                if (!db.checkPlayerExistsByName(server_id, name)) {
                    Player player = new Player(server_id, name);
                    playerList.add(player);
                }
            }
            if (!playerList.isEmpty()) {
                for (Player player : playerList) {
                    getUUID(player);
                }
            }
        }
    }

    private void queryTheServer(Server server) {
        QueryServer queryServer = new QueryServer();
        queryServer.delegate = this;
        queryServer.execute(server);
    }

    private void getPlayerFace(Player player) {
        DownloadPlayerFace downloadPlayerFace = new DownloadPlayerFace();
        downloadPlayerFace.delegate = this;
        downloadPlayerFace.execute(player);
    }

    private void getUUID(Player player) {
        DownloadPlayerUUID downloadPlayerUUID = new DownloadPlayerUUID();
        downloadPlayerUUID.delegate = this;
        downloadPlayerUUID.execute(player);
    }

    @Override
    public void downloadPlayerFaceProcessFinish(Player result) {
        db.updatePlayer(result);
        getGetPlayerFaceCountFinished = getGetPlayerFaceCountFinished + 1;
        if (getGetPlayerFaceCountFinished == getPlayerFaceCount) {
            refreshPlayerListView();
            getPlayerFaceCount = 0;
            getGetPlayerFaceCountFinished = 0;
        }
    }

    @Override
    public void downloadPlayerUUIDProcessFinish(Player result) {
        db.addPlayer(result);
    }

    @Override
    public void queryProcessFinish(Server server) {
        db.updateServer(server);
        updateCurrentPlayerList(server);
        if (mAdapter != null) {
            setCurrentPlayersOnline();
            refreshPlayerListView();
        }
        currentServer = db.getServer(server_id);
        swipeRefreshLayout.setRefreshing(false);
    }
}
