package edu.ncepu.yutao.Persistence;

import java.io.*;
import java.util.LinkedList;

/**
 * Created by AUTOY on 2016/6/2.
 */
public class UserList {
    LinkedList<String> list = null;

    void readListFromFile(String path) {
        String userFilePath = path + "/.userInfo";
        File file = new File(userFilePath);
        try {
            if (!file.exists()) {
                list = new LinkedList<>();
                if (!file.createNewFile()) {
                    throw new Exception();
                }
            } else {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(userFilePath));
                list = (LinkedList<String>) inputStream.readObject();
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void saveListToFile(String path) {
        String userFilePath = path + "/.userInfo";
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(userFilePath));
            outputStream.writeObject(list);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<String> getList() {
        return list;
    }


    public void rearrangeUser(String name) {
        if (list.contains(name)) {
            list.remove(name);
        }
        list.addFirst(name);
    }
}
