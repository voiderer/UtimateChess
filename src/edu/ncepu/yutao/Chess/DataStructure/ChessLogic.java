package edu.ncepu.yutao.Chess.DataStructure;

import edu.ncepu.yutao.Chess.UI.PieceLabel;

/**
 * Created by AUTOY on 2016/6/13.
 */
public class ChessLogic {
    private PieceLabel[][] matrix;

    public void setMatrix(PieceLabel[][] matrix) {
        this.matrix = matrix;
    }

    public boolean tryMoveChess(PieceLabel a, PieceLabel b) {
        switch (a.type) {
            case KING:
                return tryMoveKing(a, b);
            case ADVISER:
                return tryMoveAdviser(a, b);
            case BISHOP:
                return tryMoveBishop(a, b);
            case KNIGHT:
                return tryMoveKnight(a, b);
            case ROOK:
                return tryMoveRook(a, b);
            case CANNON:
                return tryMoveCannon(a, b);
            case PAWN:
                return tryMovePawn(a, b);
            case BLANK:
                break;
        }
        return false;
    }

    private boolean tryMoveKing(PieceLabel a, PieceLabel b) {
        int ax = a.x;
        int ay = a.y;
        int bx = b.x;
        int by = b.y;
        if (matrix[bx][by].type == ChessType.KING) {
            return countObstacle(ax, ay, bx, by) == 0;
        } else if (by < 3 || by > 5 || bx < 7) {
            return false;
        }
        return (ax - bx) * (ax - bx) + (ay - by) * (ay - by) == 1;
    }

    private boolean tryMoveAdviser(PieceLabel a, PieceLabel b) {
        int ax = a.x;
        int ay = a.y;
        int bx = b.x;
        int by = b.y;
        return by > 2 && by < 6 && bx > 6 && (ax - bx) * (ax - bx) + (ay - by) * (ay - by) == 2;
    }

    private boolean tryMoveBishop(PieceLabel a, PieceLabel b) {
        int ax = a.x;
        int ay = a.y;
        int bx = b.x;
        int by = b.y;
        return bx >= 5 && (ax - bx) * (ax - bx) + (ay - by) * (ay - by) == 8 && matrix[(ax + bx) / 2][(ay + by) / 2].player == ChessPlayer.BLANK;
    }

    private boolean tryMoveKnight(PieceLabel a, PieceLabel b) {
        int ax = a.x;
        int ay = a.y;
        int bx = b.x;
        int by = b.y;
        if (Math.abs(ax - bx) == 1 && Math.abs(ay - by) == 2) {
            if (matrix[ax][ay + (by - ay) / Math.abs(by - ay)].player != ChessPlayer.BLANK) {
                return false;
            }
        } else if (Math.abs(ax - bx) == 2 && Math.abs(ay - by) == 1) {
            if (matrix[ax + (bx - ax) / Math.abs(bx - ax)][ay].player != ChessPlayer.BLANK) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean tryMoveRook(PieceLabel a, PieceLabel b) {
        return countObstacle(a.x, a.y, b.x, b.y) == 0;
    }

    private boolean tryMoveCannon(PieceLabel a, PieceLabel b) {
        int ax = a.x;
        int ay = a.y;
        int bx = b.x;
        int by = b.y;
        int n = countObstacle(ax, ay, bx, by);
        if (n == -1) {
            return false;
        } else if (n == 0) {
            return b.state == ChessState.VIRTUAL || b.player == ChessPlayer.BLANK;
        }
        return n == 1 && b.player != ChessPlayer.BLANK;
    }

    private boolean tryMovePawn(PieceLabel a, PieceLabel b) {
        int ax = a.x;
        int ay = a.y;
        int bx = b.x;
        int by = b.y;
        if ((ax - bx) * (ax - bx) + (ay - by) * (ay - by) != 1) {
            return false;
        }
        if (ax > 4 && ax - bx != 1) {
            return false;
        }
        if (ax < 5) {
            if (ax == bx && Math.abs(ay - by) != 1) {
                return false;
            }
            if (ay == by && ax - bx != 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * this method is to count how many chesses lies between to points exclusively.
     *
     * @param ax
     * @param ay
     * @param bx
     * @param by
     * @return if returns -1, it means the two points are not on the same line.
     * when it returns a value greater than -1, it means the number of obstacles.
     */
    private int countObstacle(int ax, int ay, int bx, int by) {
        int i, j, k, n = 0;
        if (ax == bx) {
            i = ay < by ? ay : by;
            j = ay > by ? ay : by;
            for (k = i + 1; k < j; k++) {
                if (matrix[ax][k].player != ChessPlayer.BLANK) {
                    n++;
                }
            }
            return n;
        } else if (ay == by) {
            i = ax < bx ? ax : bx;
            j = ax > bx ? ax : bx;
            for (k = i + 1; k < j; k++) {
                if (matrix[k][ay].player != ChessPlayer.BLANK) {
                    n++;
                }
            }
            return n;
        }
        return -1;
    }
}
