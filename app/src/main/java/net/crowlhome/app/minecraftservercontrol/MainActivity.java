package net.crowlhome.app.minecraftservercontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, QueryServerResponse,
        DownloadServerImageResponse {

    private DatabaseHandler db;
    private ListView list;
    private ServerAdapter mAdapter;
    private QueryServer queryServer;
    private DownloadServerImage downloadServerImage;
    private int prevNumServers;
    private int numServers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DatabaseHandler(this);
        getAllServers();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddServerActivity.class);
                startActivity(i);
                queryAllServers();
                refreshServerIcons();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            queryAllServers();
        } catch (Exception except) {
            except.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_query_server_button:
                List<Server> servers = db.getAllServers();
                for (Server server : servers) {
                    queryServer = new QueryServer();
                    queryServer.delegate = this;
                    queryServer.execute(server);
                }
                mAdapter.clear();
                mAdapter.addAll(servers);
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_refresh_server_list_button:
                List<Server> servers1 = db.getAllServers();
                mAdapter.clear();
                mAdapter.addAll(servers1);
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.update_server_icon_button:
                List<Server> servers2 = db.getAllServers();
                for (Server server : servers2) {
                    downloadServerImage = new DownloadServerImage();
                    downloadServerImage.delegate = this;
                    downloadServerImage.execute(server);
                }
                mAdapter.clear();
                mAdapter.addAll(servers2);
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_delete_all_servers:
                List<Server> servers3 = db.getAllServers();
                for (Server server : servers3) {
                    db.deleteServer(server);
                }
                mAdapter.clear();
                mAdapter.addAll(servers3);
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void refreshServerList() {
        List<Server> servers = db.getAllServers();
        mAdapter.clear();
        mAdapter.addAll(servers);
        mAdapter.notifyDataSetChanged();
    }

    private void getAllServers() {
        List<Server> servers = db.getAllServers();
        mAdapter = new ServerAdapter(this, (ArrayList<Server>) servers);
        list = (ListView) findViewById(R.id.server_list);
        list.setAdapter(mAdapter);
    }

    public void queryAllServers() {
        List<Server> servers = db.getAllServers();
        for (Server server : servers) {
            queryServer = new QueryServer();
            queryServer.delegate = this;
            queryServer.execute(server);
        }
    }

    public void refreshServerIcons() {
        List<Server> servers2 = db.getAllServers();
        for (Server server : servers2) {
            downloadServerImage = new DownloadServerImage();
            downloadServerImage.delegate = this;
            downloadServerImage.execute(server);
        }
    }

    @Override
    public void queryProcessFinish(Server result) {
        if (result.get_serverIcon() == null) {
            downloadServerImage = new DownloadServerImage();
            downloadServerImage.delegate = this;
            downloadServerImage.execute(result);
        }
        db.updateServer(result);
        refreshServerList();
    }

    @Override
    public void downloadServerImageProcessFinish(Server result) {
        if (result != null) {
            db.updateServer(result);
        }
        refreshServerList();
    }


}
