package edu.ncepu.yutao.Network.DataStructure;

import java.net.InetAddress;

/**
 * Created by AUTOY on 2016/5/7.
 */
public class PeerRecord {
    public InetAddress address;
    public String name;
    public int port;

    protected PeerRecord() {

    }

    public PeerRecord(InetAddress Address, String name, int port) {
        this.address = Address;
        this.name = name;
        this.port = port;
    }

    public PeerRecord(PeerRecord record) {
        this.address = record.address;
        this.name = record.name;
        this.port = record.port;
    }

    public static PeerRecord readString(String[] strings, int i) {
        if (strings.length - i < 3) {
            return null;
        }
        try {
            PeerRecord record = new PeerRecord();
            record.address = InetAddress.getByName(strings[i]);
            record.port = Integer.parseInt(strings[i + 1]);
            record.name = strings[i + 2];
            return record;
        } catch (Exception e) {
            return null;
        }
    }

    public static PeerRecord readID(String[] strings, int i) {
        if (strings.length - i < 2) {
            return null;
        }
        try {
            PeerRecord record = new PeerRecord();
            record.address = InetAddress.getByName(strings[i]);
            record.port = Integer.parseInt(strings[i + 1]);
            record.name = "";
            return record;
        } catch (Exception e) {
            return null;
        }
    }

    public String toString() {
        return address.getHostAddress() + "|" + port + "|" + name;
    }

    public String getId() {
        return address.getHostAddress() + "|" + port;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PeerRecord) {
            PeerRecord record = (PeerRecord) obj;
            return record.getId().equals(getId());
        }
        return false;
    }
}
