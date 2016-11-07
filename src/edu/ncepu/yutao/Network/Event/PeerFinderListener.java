package edu.ncepu.yutao.Network.Event;


import edu.ncepu.yutao.Network.DataStructure.PeerList;
import edu.ncepu.yutao.Network.DataStructure.RoomList;

import java.util.EventListener;

/**
 * Created by AUTOY on 2016/6/4.
 */
public interface PeerFinderListener extends EventListener {
    void peerChanged(PeerList list);

    void roomChanged(RoomList list);

    void chatReceived(String s);
}
