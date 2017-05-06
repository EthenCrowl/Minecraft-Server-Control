package net.crowlhome.app.minecraftservercontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by ethen on 4/30/17.
 * Copyright Ethen Crowl
 */

public class ServerPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    int mServer_id;

    public ServerPagerAdapter(FragmentManager fm, int numOfTabs, int server_id) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
        this.mServer_id = server_id;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Bundle bundle = new Bundle();
                bundle.putInt("SERVER_ID", mServer_id);
                ServerPlayerListFragment player_list_fragment = new ServerPlayerListFragment();
                player_list_fragment.setArguments(bundle);
                return player_list_fragment;
            case 1:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("SERVER_ID", mServer_id);
                ServerCommandButtonsFragment command_buttons_fragment = new
                        ServerCommandButtonsFragment();
                command_buttons_fragment.setArguments(bundle1);
                return command_buttons_fragment;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("SERVER_ID", mServer_id);
        ServerPlayerListFragment player_list_fragment = new ServerPlayerListFragment();
        player_list_fragment.setArguments(bundle);
        return player_list_fragment;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
