package net.crowlhome.app.minecraftservercontrol;

import java.util.List;

/**
 * Created by ethen on 3/12/17.
 * Copyright Ethen Crowl
 */

public interface QueryAllServersResponse {
    void queryAllServersProcessFinish(List<Server> output);
}
