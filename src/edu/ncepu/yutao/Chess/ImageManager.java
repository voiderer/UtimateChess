package edu.ncepu.yutao.Chess;


import edu.ncepu.yutao.Chess.DataStructure.ChessPlayer;
import edu.ncepu.yutao.Chess.DataStructure.ChessState;
import edu.ncepu.yutao.Chess.DataStructure.ChessType;

import javax.swing.*;

/**
 * Created by AUTOY on 2016/6/11.
 */
public class ImageManager {
    public static ImageIcon icons[][][] = new ImageIcon[3][2][7];
    public static ImageIcon justMovedIcon;
    public static ImageIcon icon;

    static {
        justMovedIcon = new ImageIcon(ImageManager.class.getResource("/image/empty.png"));

        icons[0][0][0] = new ImageIcon(ImageManager.class.getResource("/image/黑将.png"));
        icons[0][0][1] = new ImageIcon(ImageManager.class.getResource("/image/黑士.png"));
        icons[0][0][2] = new ImageIcon(ImageManager.class.getResource("/image/黑象.png"));
        icons[0][0][3] = new ImageIcon(ImageManager.class.getResource("/image/黑马.png"));
        icons[0][0][4] = new ImageIcon(ImageManager.class.getResource("/image/黑车.png"));
        icons[0][0][5] = new ImageIcon(ImageManager.class.getResource("/image/黑炮.png"));
        icons[0][0][6] = new ImageIcon(ImageManager.class.getResource("/image/黑卒.png"));
        icons[0][1][0] = new ImageIcon(ImageManager.class.getResource("/image/红将.png"));
        icons[0][1][1] = new ImageIcon(ImageManager.class.getResource("/image/红士.png"));
        icons[0][1][2] = new ImageIcon(ImageManager.class.getResource("/image/红象.png"));
        icons[0][1][3] = new ImageIcon(ImageManager.class.getResource("/image/红马.png"));
        icons[0][1][4] = new ImageIcon(ImageManager.class.getResource("/image/红车.png"));
        icons[0][1][5] = new ImageIcon(ImageManager.class.getResource("/image/红炮.png"));
        icons[0][1][6] = new ImageIcon(ImageManager.class.getResource("/image/红卒.png"));

        icons[1][0][0] = new ImageIcon(ImageManager.class.getResource("/image/黑将1.png"));
        icons[1][0][1] = new ImageIcon(ImageManager.class.getResource("/image/黑士1.png"));
        icons[1][0][2] = new ImageIcon(ImageManager.class.getResource("/image/黑象1.png"));
        icons[1][0][3] = new ImageIcon(ImageManager.class.getResource("/image/黑马1.png"));
        icons[1][0][4] = new ImageIcon(ImageManager.class.getResource("/image/黑车1.png"));
        icons[1][0][5] = new ImageIcon(ImageManager.class.getResource("/image/黑炮1.png"));
        icons[1][0][6] = new ImageIcon(ImageManager.class.getResource("/image/黑卒1.png"));
        icon =
                icons[1][1][0] = new ImageIcon(ImageManager.class.getResource("/image/红将1.png"));
        icons[1][1][1] = new ImageIcon(ImageManager.class.getResource("/image/红士1.png"));
        icons[1][1][2] = new ImageIcon(ImageManager.class.getResource("/image/红象1.png"));
        icons[1][1][3] = new ImageIcon(ImageManager.class.getResource("/image/红马1.png"));
        icons[1][1][4] = new ImageIcon(ImageManager.class.getResource("/image/红车1.png"));
        icons[1][1][5] = new ImageIcon(ImageManager.class.getResource("/image/红炮1.png"));
        icons[1][1][6] = new ImageIcon(ImageManager.class.getResource("/image/红卒1.png"));

        icons[2][0][0] = new ImageIcon(ImageManager.class.getResource("/image/黑将2.png"));
        icons[2][0][1] = new ImageIcon(ImageManager.class.getResource("/image/黑士2.png"));
        icons[2][0][2] = new ImageIcon(ImageManager.class.getResource("/image/黑象2.png"));
        icons[2][0][3] = new ImageIcon(ImageManager.class.getResource("/image/黑马2.png"));
        icons[2][0][4] = new ImageIcon(ImageManager.class.getResource("/image/黑车2.png"));
        icons[2][0][5] = new ImageIcon(ImageManager.class.getResource("/image/黑炮2.png"));
        icons[2][0][6] = new ImageIcon(ImageManager.class.getResource("/image/黑卒2.png"));
        icons[2][1][0] = new ImageIcon(ImageManager.class.getResource("/image/红将2.png"));
        icons[2][1][1] = new ImageIcon(ImageManager.class.getResource("/image/红士2.png"));
        icons[2][1][2] = new ImageIcon(ImageManager.class.getResource("/image/红象2.png"));
        icons[2][1][3] = new ImageIcon(ImageManager.class.getResource("/image/红马2.png"));
        icons[2][1][4] = new ImageIcon(ImageManager.class.getResource("/image/红车2.png"));
        icons[2][1][5] = new ImageIcon(ImageManager.class.getResource("/image/红炮2.png"));
        icons[2][1][6] = new ImageIcon(ImageManager.class.getResource("/image/红卒2.png"));
    }

    public static ImageIcon getImage(ChessState state, ChessPlayer player, ChessType type) {
        if (state == ChessState.VIRTUALLY_CHOSEN)
            state = ChessState.CHOSEN;
        else if (state == ChessState.JUST_MOVED) {
            return ImageManager.justMovedIcon;
        }
        int i = state.ordinal();
        if (i > 2)
            return null;
        int j = player.ordinal();
        if (j > 1)
            return null;
        int k = type.ordinal();
        if (k > 6) {
            return null;
        }
        return icons[i][j][k];
    }
}
