package edu.ncepu.yutao.Persistence;

import edu.ncepu.yutao.Chess.DataStructure.ChessHistory;

import java.io.*;
import java.util.LinkedList;

/**
 * Created by AUTOY on 2016/6/1.
 */
public class ChessData {
    String userName;
    LinkedList<ChessHistory> histories;
    String path;

    public void readUserData(String path, String name) {
        File file = new File(path);
        this.path = path;
        try {
            if (!file.exists()) {
                userName = name;
                histories = new LinkedList<>();
                file.createNewFile();
            } else {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path));
                userName = (String) inputStream.readObject();
                histories = (LinkedList<ChessHistory>) inputStream.readObject();
                inputStream.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveUserData() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path));
            outputStream.writeObject(userName);
            outputStream.writeObject(histories);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        return userName;
    }
}

