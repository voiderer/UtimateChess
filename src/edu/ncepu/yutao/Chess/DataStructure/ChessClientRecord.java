package edu.ncepu.yutao.Chess.DataStructure;

import edu.ncepu.yutao.Network.DataStructure.PeerRecord;

import java.net.InetAddress;

/**
 * Created by AUTOY on 2016/6/5.
 */
public class ChessClientRecord extends PeerRecord {
    public ChessPlayer type;

    protected ChessClientRecord() {

    }

    public ChessClientRecord(PeerRecord record, ChessPlayer type) {
        super(record);
        this.type = type;
    }

    public ChessClientRecord(InetAddress Address, String name, int clientPort, ChessPlayer type) {
        super(Address, name, clientPort);
        this.type = type;
    }

    public static ChessClientRecord readString(String[] strings, int i) {
        if (strings.length - i < 4) {
            return null;
        }
        try {
            ChessClientRecord record = new ChessClientRecord();
            record.address = InetAddress.getByName(strings[i]);
            record.port = Integer.parseInt(strings[i + 1]);
            record.name = strings[i + 2];
            record.type = ChessPlayer.readString(strings[i + 3]);
            return record;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return super.toString() + "|" + type;
    }
}

