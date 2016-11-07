package edu.ncepu.yutao.Network.DataStructure;


import java.util.Collection;
import java.util.TreeMap;

/**
 * Created by AUTOY on 2016/5/7.
 */
public class RoomList {
    TreeMap<String, RoomRecord> serverMap = new TreeMap<>();

    public void put(RoomRecord record) {
        serverMap.put(record.getId(), record);
    }

    public void clear() {
        serverMap.clear();
    }


    public RoomRecord getServerInformation(String s) {
        return serverMap.get(s);
    }

    public Collection<RoomRecord> getCollection() {
        return serverMap.values();
    }

    public void remove(String s) {
        serverMap.remove(s);
    }
}
