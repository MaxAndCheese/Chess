package Pieces;
import GameLogic.*;
import Pieces.PieceLogic.*;

public class Bishop extends Piece {

    public Bishop(Position startPos, PieceColor c) {
        super("Bishop", startPos, c);
    }

    @Override
    public boolean canMove(Position p) {
        if (isTaken() || (Board.getPieceAtPos(p) != null && Board.getPieceAtPos(p).getColor() == getColor())) return false;
        return checkAllDiagonal(p);
    }
}
