package edu.ncepu.yutao.Chess.UI;

import edu.ncepu.yutao.Chess.DataStructure.*;
import edu.ncepu.yutao.Chess.SoundManifest;
import edu.ncepu.yutao.Chess.SoundThread;
import edu.ncepu.yutao.Chess.UI.Event.ChessPlateListener;
import javafx.util.Pair;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.util.LinkedList;

/**
 * Created by AUTOY on 2016/5/5.
 */
class BoardData {
    final private String initString = "rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR";
    private PieceLabel[][] matrix = new PieceLabel[10][9];
    private LinkedList<String> historyList = new LinkedList<>();

    /**
     * this attribute is a bimap form a Character to the type and player of a chess.
     * the map is needed when reading the plate from a sting or save the plate into a string.
     */
    private DualHashBidiMap<Character, Pair<ChessPlayer, ChessType>> chessMap;
    private DualHashBidiMap<Integer, Character> rowMap, colMap;
    private String lastMove;
    private boolean hasChosen = false;
    private PieceLabel chosen = null;
    private PieceLabel lastMoved1, lastMoved2;
    private boolean myTurn;
    private boolean hasLastMove = false;
    private ChessPlayer side = ChessPlayer.BLANK;
    private ChessLogic logic = new ChessLogic();
    private ChessPlateListener chessPlateListener = null;

    BoardData(PieceLabel[][] matrix) {
        hasLastMove = false;
        this.matrix = matrix;
        chessMap = ChessOrdinateMap.chessMap;
        logic.setMatrix(matrix);
    }

    void setChessPlateListener(ChessPlateListener chessPlateListener) {
        this.chessPlateListener = chessPlateListener;
    }

    void onMouseEnter(PieceLabel label) {
        if (label.player == ChessPlayer.BLANK) {
            if (hasChosen) {
                if (logic.tryMoveChess(chosen, label)) {
                    label.player = chosen.player;
                    label.type = chosen.type;
                    label.setState(ChessState.VIRTUAL);
                }
            }
        } else if (label.player == side) {
            if (label.state != ChessState.CHOSEN) {
                label.setState(ChessState.VIRTUALLY_CHOSEN);
            }
        } else {
            if (hasChosen) {
                if (logic.tryMoveChess(chosen, label)) {
                    label.setState(ChessState.VIRTUAL);
                }
            }
        }
    }

    void onMouseExit(PieceLabel label) {
        if (label.state == ChessState.VIRTUAL) {
            if (label.player == side) {
                label.player = ChessPlayer.BLANK;
                label.state = ChessState.BLANK;
                label.setState(ChessState.BLANK);
            } else {
                label.setState(ChessState.NORMAL);
            }
        } else if (label.player == side) {
            if (label.state == ChessState.VIRTUALLY_CHOSEN) {
                label.setState(ChessState.NORMAL);
            }
        } else {
            if (hasChosen) {
                label.setState(ChessState.NORMAL);
            }
        }
    }

    void onMousePress(PieceLabel label) {
        if (hasChosen && label != chosen) {
            if (label.state == ChessState.VIRTUAL) {
                if (tryMoveChess(chosen, label)) {
                    chessPlateListener.chessMoved(lastMove);

                }

            } else if (label.player == side) {
                chosen.setState(ChessState.NORMAL);
                label.setState(ChessState.CHOSEN);
                chosen = label;
            } else {
                SoundThread.playOnce(SoundManifest.ILLEGAL);
            }
        } else if (label.player == side) {
            hasChosen = true;
            label.setState(ChessState.CHOSEN);
            chosen = label;
        }
    }

    private void setPlayerRed() {
        side = ChessPlayer.RED;
        rowMap = ChessOrdinateMap.redRowMap;
        colMap = ChessOrdinateMap.redColMap;
        myTurn = true;
        readFromString(initString);
    }

    private void setPlayerBlue() {
        side = ChessPlayer.BLUE;
        rowMap = ChessOrdinateMap.blueRowMap;
        colMap = ChessOrdinateMap.blueColMap;
        myTurn = false;
        readFromString(initString);
    }

    void setPlayerBlank() {
        side = ChessPlayer.BLANK;
        rowMap = ChessOrdinateMap.redRowMap;
        colMap = ChessOrdinateMap.redColMap;
        myTurn = false;
    }

    private boolean tryMoveChess(PieceLabel a, PieceLabel b) {
        if (logic.tryMoveChess(a, b)) {
            updateLastMove(a, b);
            myTurn = false;
            return true;
        } else {
            return false;
        }
    }

    void readMove(String s) {
        int y1 = colMap.getKey(s.charAt(0));
        int x1 = rowMap.getKey(s.charAt(1));
        int y2 = colMap.getKey(s.charAt(2));
        int x2 = rowMap.getKey(s.charAt(3));
        PieceLabel a = matrix[x1][y1];
        PieceLabel b = matrix[x2][y2];
        updateLastMove(a, b);
        if (a.player == side) {
            SoundThread.playOnce(SoundManifest.CAPTURE);
        } else {
            SoundThread.playOnce(SoundManifest.MOVE);
        }
        if (side != ChessPlayer.BLANK) {
            myTurn = true;
        }
    }

    boolean isMyTurn() {
        return myTurn;
    }

    void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }


    boolean readFromString(String fen) {
        String[] split = fen.split(" ");
        String[] cheeses = split[0].split("/");
        if (cheeses.length != 10) {
            return false;
        }
        Character a, b, c;
        String temp;
        PieceLabel chess;
        Pair<ChessPlayer, ChessType> pair;
        int i, j, k, m;
        for (a = '9', i = 0; i < 10; a--, i++) {
            temp = cheeses[i];
            j = 0;
            b = 'a';
            for (k = 0; k < temp.length(); k++) {
                c = temp.charAt(k);
                pair = chessMap.get(c);
                if (pair != null) {
                    chess = matrix[rowMap.getKey(a)][colMap.getKey(b)];
                    chess.setAll(pair.getValue(), pair.getKey(), ChessState.NORMAL);
                    j++;
                    b++;
                } else if (c >= '0' & c <= '9') {
                    m = c - '0' + j;
                    for (; j < m; j++, b++) {
                        matrix[rowMap.getKey(a)][colMap.getKey(b)].setAll(ChessType.BLANK, ChessPlayer.BLANK, ChessState.NORMAL);
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateLastMove(PieceLabel a, PieceLabel b) {
        if (hasLastMove) {
            lastMoved1.setState(ChessState.NORMAL);
            lastMoved2.setState(ChessState.NORMAL);
        }

        lastMove = "" + colMap.get(a.y) + rowMap.get(a.x) + colMap.get(b.y) + rowMap.get(b.x);
        Character bc = chessMap.getKey(new Pair<>(b.player, b.type));
        if (b.state == ChessState.VIRTUAL && b.player == side || bc == null) {
            bc = '0';
        }
        historyList.add(lastMove + bc);
        lastMoved1 = a;
        lastMoved2 = b;
        b.setAll(a.type, a.player, ChessState.CHOSEN);
        a.setAll(ChessType.BLANK, ChessPlayer.BLANK, ChessState.JUST_MOVED);
        hasChosen = false;
        hasLastMove = true;
    }

    void revokeLast() {
        String last = historyList.removeLast();
        int y1 = colMap.getKey(last.charAt(0));
        int x1 = rowMap.getKey(last.charAt(1));
        int y2 = colMap.getKey(last.charAt(2));
        int x2 = rowMap.getKey(last.charAt(3));
        PieceLabel a, b;
        a = matrix[x1][y1];
        b = matrix[x2][y2];
        if (last.charAt(4) == '0') {
            a.setAll(b.type, b.player, ChessState.NORMAL);
            b.setAll(ChessType.BLANK, ChessPlayer.BLANK, ChessState.NORMAL);

        } else {
            Pair<ChessPlayer, ChessType> pair = chessMap.get(last.charAt(4));
            a.setAll(b.type, b.player, ChessState.NORMAL);
            b.setAll(pair.getValue(), pair.getKey(), ChessState.NORMAL);
        }
        lastMoved1.setState(ChessState.NORMAL);
        lastMoved2.setState(ChessState.NORMAL);


        if (!historyList.isEmpty()) {
            last = historyList.getLast();
            y1 = colMap.getKey(last.charAt(0));
            x1 = rowMap.getKey(last.charAt(1));
            y2 = colMap.getKey(last.charAt(2));
            x2 = rowMap.getKey(last.charAt(3));
            a = matrix[x1][y1];
            b = matrix[x2][y2];
            a.setState(ChessState.JUST_MOVED);
            b.setState(ChessState.CHOSEN);
            lastMoved1 = a;
            lastMoved2 = b;
        } else {
            hasLastMove = false;
        }
        if (side == ChessPlayer.RED && historyList.size() < 1) {
            chessPlateListener.revokeUsedUp();
        }
        if (side == ChessPlayer.BLUE && historyList.size() < 2) {
            chessPlateListener.revokeUsedUp();
        }
        myTurn = !myTurn;
    }

    void handleStart(String string) {
        if (string.equals(ChessPlayer.RED.toString())) {
            setPlayerRed();
        } else if (string.equals(ChessPlayer.BLUE.toString())) {
            setPlayerBlue();
        } else {
            side = ChessPlayer.BLANK;
            setMyTurn(false);
        }
    }
}
