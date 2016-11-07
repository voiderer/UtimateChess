package edu.ncepu.yutao.Network;

/**
 * Created by AUTOY on 2016/5/7.
 */

public interface PeerFinderInterface {
    int PEER_FINDER_START = 13000;
    int PEER_FINDER_ID_START = 13500;
    String MULTICAST_ADDRESS = "225.0.0.1";
    String PEER_KNOCK = "PEER_KNOCK";
    String PEER_CHANGE = "PEER_CHANGE";
    String PEER_LEAVE = "PEER_LEAVE";
    String ROOM_KNOCK = "ROOM_KNOCK";
    String ROOM_CHANGE = "ROOM_CHANGE";
    String ROOM_LEAVE = "ROOM_LEAVE";
    String UNIVERSAL_CHAT = "CHAT";
}