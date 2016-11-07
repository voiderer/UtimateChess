package edu.ncepu.yutao.Chess.UI;


import edu.ncepu.yutao.Chess.UI.Event.ChessPlateListener;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by AUTOY on 2016/4/26.
 */
public class ChessBoardPanel extends JPanel implements MouseListener {
    private PieceLabel matrix[][] = new PieceLabel[10][9];
    private BoardData boardData;

    public ChessBoardPanel() {
        super(null);
        int i, j;
        PieceLabel label;
        boardData = new BoardData(matrix);
        this.setPreferredSize(new Dimension(590, 651));
        for (i = 0; i < 10; i++) {
            for (j = 0; j < 9; j++) {
                label = matrix[i][j] = new PieceLabel(i, j);
                label.setBounds(j * 60 + 27, i * 60 + 27, 60, 60);
                label.setUI(new BasicLabelUI());
                label.addMouseListener(this);
                add(label);
            }
        }

        JLabel image;
        add(image = new JLabel(new ImageIcon(ChessBoardPanel.class.getResource("/image/main.png"))));
        image.setBounds(0, 0, 590, 651);
    }


    public void setChessPlateListener(ChessPlateListener chessPlateListener) {
        boardData.setChessPlateListener(chessPlateListener);
    }


    @Override
    public void mouseClicked(MouseEvent e) {


    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!boardData.isMyTurn())
            return;
        PieceLabel label = (PieceLabel) e.getSource();
        boardData.onMousePress(label);
        this.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (!boardData.isMyTurn())
            return;
        PieceLabel label = (PieceLabel) e.getSource();
        boardData.onMouseEnter(label);
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (!boardData.isMyTurn())
            return;
        PieceLabel label = (PieceLabel) e.getSource();
        boardData.onMouseExit(label);
        this.repaint();
    }

    public void readFromString(String string) {
        boardData.setPlayerBlank();
        boardData.setMyTurn(false);
        boardData.readFromString(string);
        repaint();
    }

    public void handleMove(String string) {
        boardData.readMove(string);
        repaint();
    }

    public void handleStart(String string) {
        boardData.handleStart(string);
        repaint();
    }

    public void setMovable(boolean b) {
        boardData.setMyTurn(b);
    }

    public void revokeMove(int i) {
        for (int j = 0; j < i; j++) {
            boardData.revokeLast();
        }
    }
}

