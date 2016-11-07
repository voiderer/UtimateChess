package edu.ncepu.yutao.Network.Event;

import edu.ncepu.yutao.Network.DataStructure.RoomRecord;

/**
 * Created by AUTOY on 2016/6/10.
 */

public interface ServerListener {
    void roomRecordChanged(RoomRecord record);

    void roomDestroyed(RoomRecord record);
}
