package edu.ncepu.yutao.Persistence;

import java.io.File;

/**
 * Created by AUTOY on 2016/6/1.
 */
public class PersistenceManager {
    private static ChessData chessData = new ChessData();
    private static UserList userList = new UserList();
    private static String directory;

    public static void readUserList() {
        directory = System.getProperty("user.dir") + "/.Users";
        File file = new File(directory);
        if (!file.exists()) {
            file.mkdir();
        }
        userList.readListFromFile(directory);
    }

    public static void saveUserList() {
        userList.saveListToFile(directory);
    }

    public static void readUserData(String name) {
        userList.rearrangeUser(name);
        String path = directory + "/" + name;
        chessData.readUserData(path, name);
    }

    public static UserList getUserList() {
        return userList;
    }

    public static void saveChessData() {
        chessData.saveUserData();
    }

    public static ChessData getData() {
        return chessData;
    }

    public static String getUserName() {
        return chessData.getUserName();
    }
}
