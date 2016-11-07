package edu.ncepu.yutao.Chess.DataStructure;

import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by AUTOY on 2016/5/5.
 */
public class ChessHistory implements Serializable {
    private LinkedList<String> historyList = new LinkedList<>();
    private Character[][] matrix = new Character[10][9];
    private ChessPlayer currentSide = ChessPlayer.BLANK;
    private DualHashBidiMap<Integer, Character> rowMap = new DualHashBidiMap<>();
    private DualHashBidiMap<Integer, Character> colMap = new DualHashBidiMap<>();

    public ChessHistory() {
        rowMap.clear();
        rowMap.put(0, '9');
        rowMap.put(1, '8');
        rowMap.put(2, '7');
        rowMap.put(3, '6');
        rowMap.put(4, '5');
        rowMap.put(5, '4');
        rowMap.put(6, '3');
        rowMap.put(7, '2');
        rowMap.put(8, '1');
        rowMap.put(9, '0');
        colMap.clear();
        colMap.put(0, 'a');
        colMap.put(1, 'b');
        colMap.put(2, 'c');
        colMap.put(3, 'd');
        colMap.put(4, 'e');
        colMap.put(5, 'f');
        colMap.put(6, 'g');
        colMap.put(7, 'h');
        colMap.put(8, 'i');
        currentSide = ChessPlayer.RED;
    }

    public boolean readFromString(String fen) {
        String[] split = fen.split(" ");
        String[] cheeses = split[0].split("/");
        if (cheeses.length != 10) {
            return false;
        }
        Character a, b, c;
        String temp;
        int i, j, k, m;
        for (a = '9', i = 0; i < 10; a--, i++) {
            temp = cheeses[i];
            j = 0;
            b = 'a';
            for (k = 0; k < temp.length(); k++) {
                c = temp.charAt(k);
                if (c.toString().matches("[rnbakcpRNBAKCP]")) {
                    matrix[rowMap.getKey(a)][colMap.getKey(b)] = c;
                    j++;
                    b++;
                } else if (c >= '0' & c <= '9') {
                    m = c - '0' + j;
                    for (; j < m; j++, b++) {
                        matrix[rowMap.getKey(a)][colMap.getKey(b)] = 0;
                    }
                } else {
                    return false;
                }
            }
        }
        historyList.clear();
        currentSide = ChessPlayer.RED;
        return true;
    }

    public String saveToString() {
        Character a, b, c;
        int i;
        String s = "";
        for (a = '9'; a != '0' - 1; a--) {
            i = 0;
            for (b = 'a'; b != 'j'; b++) {
                c = matrix[rowMap.getKey(a)][colMap.getKey(b)];
                if (c == 0) {
                    i++;
                } else {
                    if (i != 0) {
                        s += i;
                        i = 0;
                    }
                    s += c;
                }
            }
            if (i != 0) {
                s += i;
            }
            if (a != '0') {
                s += '/';
            }
        }
        return s;
    }

    public ChessPlayer readMove(String s) {
        int y1 = colMap.getKey(s.charAt(0));
        int x1 = rowMap.getKey(s.charAt(1));
        int y2 = colMap.getKey(s.charAt(2));
        int x2 = rowMap.getKey(s.charAt(3));
        Character c = matrix[x2][y2];
        historyList.add(s + c);
        matrix[x2][y2] = matrix[x1][y1];
        matrix[x1][y1] = 0;
        if (currentSide.equals(ChessPlayer.RED)) {
            currentSide = ChessPlayer.BLUE;
        } else if (currentSide.equals(ChessPlayer.BLUE)) {
            currentSide = ChessPlayer.RED;
        }
        if (c == 'K') {
            return ChessPlayer.RED;
        } else if (c == 'k') {
            return ChessPlayer.BLUE;
        } else {
            return ChessPlayer.BLANK;
        }
    }

    public void revokeMove() {
        String s = historyList.getLast();
        int y1 = colMap.getKey(s.charAt(0));
        int x1 = rowMap.getKey(s.charAt(1));
        int y2 = colMap.getKey(s.charAt(2));
        int x2 = rowMap.getKey(s.charAt(3));
        matrix[x1][y1] = matrix[x2][y2];
        matrix[x2][y2] = s.charAt(4);
        if (currentSide.equals(ChessPlayer.RED)) {
            currentSide = ChessPlayer.BLUE;
        } else if (currentSide.equals(ChessPlayer.BLUE)) {
            currentSide = ChessPlayer.RED;
        }
    }

    public int revokeLast(ChessPlayer type) {
        int i = 1;
        if (currentSide.equals(type)) {
            revokeMove();
            i++;
        }
        revokeMove();
        return i;
    }
}
