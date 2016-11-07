package edu.ncepu.yutao.Chess.DataStructure;

import javafx.util.Pair;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

/**
 * Created by AUTOY on 2016/6/12.
 */
public class ChessOrdinateMap {
    public static DualHashBidiMap<Character, Pair<ChessPlayer, ChessType>> chessMap = new DualHashBidiMap<>();
    public static DualHashBidiMap<Integer, Character> redRowMap = new DualHashBidiMap<>();
    public static DualHashBidiMap<Integer, Character> redColMap = new DualHashBidiMap<>();
    public static DualHashBidiMap<Integer, Character> blueRowMap = new DualHashBidiMap<>();
    public static DualHashBidiMap<Integer, Character> blueColMap = new DualHashBidiMap<>();

    static {
        chessMap.put('k', new Pair<>(ChessPlayer.BLUE, ChessType.KING));
        chessMap.put('a', new Pair<>(ChessPlayer.BLUE, ChessType.ADVISER));
        chessMap.put('b', new Pair<>(ChessPlayer.BLUE, ChessType.BISHOP));
        chessMap.put('n', new Pair<>(ChessPlayer.BLUE, ChessType.KNIGHT));
        chessMap.put('r', new Pair<>(ChessPlayer.BLUE, ChessType.ROOK));
        chessMap.put('c', new Pair<>(ChessPlayer.BLUE, ChessType.CANNON));
        chessMap.put('p', new Pair<>(ChessPlayer.BLUE, ChessType.PAWN));
        chessMap.put('K', new Pair<>(ChessPlayer.RED, ChessType.KING));
        chessMap.put('A', new Pair<>(ChessPlayer.RED, ChessType.ADVISER));
        chessMap.put('B', new Pair<>(ChessPlayer.RED, ChessType.BISHOP));
        chessMap.put('N', new Pair<>(ChessPlayer.RED, ChessType.KNIGHT));
        chessMap.put('R', new Pair<>(ChessPlayer.RED, ChessType.ROOK));
        chessMap.put('C', new Pair<>(ChessPlayer.RED, ChessType.CANNON));
        chessMap.put('P', new Pair<>(ChessPlayer.RED, ChessType.PAWN));

        redRowMap.put(0, '9');
        redRowMap.put(1, '8');
        redRowMap.put(2, '7');
        redRowMap.put(3, '6');
        redRowMap.put(4, '5');
        redRowMap.put(5, '4');
        redRowMap.put(6, '3');
        redRowMap.put(7, '2');
        redRowMap.put(8, '1');
        redRowMap.put(9, '0');

        redColMap.put(0, 'a');
        redColMap.put(1, 'b');
        redColMap.put(2, 'c');
        redColMap.put(3, 'd');
        redColMap.put(4, 'e');
        redColMap.put(5, 'f');
        redColMap.put(6, 'g');
        redColMap.put(7, 'h');
        redColMap.put(8, 'i');

        blueRowMap.put(0, '0');
        blueRowMap.put(1, '1');
        blueRowMap.put(2, '2');
        blueRowMap.put(3, '3');
        blueRowMap.put(4, '4');
        blueRowMap.put(5, '5');
        blueRowMap.put(6, '6');
        blueRowMap.put(7, '7');
        blueRowMap.put(8, '8');
        blueRowMap.put(9, '9');

        blueColMap.put(0, 'i');
        blueColMap.put(1, 'h');
        blueColMap.put(2, 'g');
        blueColMap.put(3, 'f');
        blueColMap.put(4, 'e');
        blueColMap.put(5, 'd');
        blueColMap.put(6, 'c');
        blueColMap.put(7, 'b');
        blueColMap.put(8, 'a');
    }
}
