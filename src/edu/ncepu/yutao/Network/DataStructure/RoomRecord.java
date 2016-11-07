package edu.ncepu.yutao.Network.DataStructure;

import java.net.InetAddress;

/**
 * Created by AUTOY on 2016/5/7.
 */
public class RoomRecord {
    public InetAddress address;
    public int port;
    public String name;
    public String type;
    public int playerCount;

    private RoomRecord() {

    }

    public RoomRecord(InetAddress address, int port, String name, String type, int playerCount) {
        this.address = address;
        this.name = name;
        this.port = port;
        this.type = type;
        this.playerCount = playerCount;
    }

    public static RoomRecord readString(String[] strings, int i) {
        if (strings.length - i < 5) {
            return null;
        }
        try {
            RoomRecord record = new RoomRecord();
            record.address = InetAddress.getByName(strings[i]);
            record.port = Integer.parseInt(strings[i + 1]);
            record.name = strings[i + 2];
            record.type = strings[i + 3];
            record.playerCount = Integer.parseInt(strings[i + 4]);
            return record;
        } catch (Exception e) {
            return null;
        }
    }

    public static RoomRecord readID(String[] strings, int i) {
        if (strings.length - i < 2) {
            return null;
        }
        try {
            RoomRecord record = new RoomRecord();
            record.address = InetAddress.getByName(strings[i]);
            record.port = Integer.parseInt(strings[i + 1]);
            record.name = "";
            return record;
        } catch (Exception e) {
            return null;
        }

    }

    public String toString() {
        return address.getHostAddress() + "|" + port + "|" + name + "|" + type + "|" + playerCount;
    }

    public String getId() {
        return address.getHostAddress() + "|" + port;
    }
}
