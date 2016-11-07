package edu.ncepu.yutao.Chess.DataStructure;

/**
 * Created by AUTOY on 2016/5/5.
 */
public enum ChessPlayer {
    BLUE,
    RED,
    BLANK,
    DEFAULT;

    public static ChessPlayer readString(String s) {
        if (s.equals("蓝方"))
            return BLUE;
        if (s.equals("红方"))
            return RED;
        if (s.equals("观战"))
            return BLANK;
        return DEFAULT;
    }

    @Override
    public String toString() {
        switch (this) {
            case BLUE:
                return "蓝方";
            case RED:
                return "红方";
            case BLANK:
                return "观战";
        }
        return "";
    }
}
