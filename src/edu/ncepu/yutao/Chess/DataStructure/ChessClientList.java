package edu.ncepu.yutao.Chess.DataStructure;

import java.util.Collection;
import java.util.TreeMap;

/**
 * Created by AUTOY on 2016/6/5.
 */
public class ChessClientList {
    private TreeMap<String, ChessClientRecord> map = new TreeMap<>();

    public void put(ChessClientRecord data) {
        map.put(data.getId(), data);
    }

    public void remove(String s) {
        map.remove(s);
    }

    public ChessClientRecord get(String s) {
        return map.get(s);
    }

    public Collection<ChessClientRecord> getCollection() {
        return map.values();
    }

    public int getCount() {
        return map.size();
    }
}
