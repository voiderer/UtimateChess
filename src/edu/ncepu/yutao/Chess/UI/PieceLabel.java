package edu.ncepu.yutao.Chess.UI;

import edu.ncepu.yutao.Chess.DataStructure.ChessPlayer;
import edu.ncepu.yutao.Chess.DataStructure.ChessState;
import edu.ncepu.yutao.Chess.DataStructure.ChessType;
import edu.ncepu.yutao.Chess.ImageManager;

import javax.swing.*;

/**
 * Created by AUTOY on 2016/6/13.
 */
public class PieceLabel extends JLabel {
    public ChessType type = ChessType.BLANK;
    public ChessPlayer player = ChessPlayer.BLANK;
    public ChessState state = ChessState.BLANK;
    public int x;
    public int y;

    public PieceLabel(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    public void setAll(ChessType type, ChessPlayer chessPlayer, ChessState state) {
        this.type = type;
        this.player = chessPlayer;
        this.state = state;
        this.setIcon(ImageManager.getImage(state, player, type));
    }

    public void setState(ChessState state) {
        this.state = state;
        this.setIcon(ImageManager.getImage(state, player, type));
    }
}
