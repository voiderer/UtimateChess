package edu.ncepu.yutao.MainClass.Event;

import edu.ncepu.yutao.Network.DataStructure.RoomRecord;

/**
 * Created by AUTOY on 2016/6/10.
 */
public interface LobbyListener {
    void makeChessRoom();

    void joinChessRoom(RoomRecord serverRecord);
}
