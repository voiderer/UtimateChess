package edu.ncepu.yutao.Network.DataStructure;

import java.util.Collection;
import java.util.TreeMap;

/**
 * Created by AUTOY on 2016/5/7.
 */
public class PeerList {
    private TreeMap<String, PeerRecord> peerMap = new TreeMap<>();

    public void put(PeerRecord peer) {
        peerMap.put(peer.getId(), peer);
    }

    public void remove(String key) {
        peerMap.remove(key);
    }

    public void clear() {
        peerMap.clear();
    }

    public int getCount() {
        return peerMap.size();
    }

    public Collection<PeerRecord> getCollection() {
        return peerMap.values();
    }

    public PeerRecord getValue(String s) {
        return peerMap.get(s);
    }
}
