package Pieces;
import GameLogic.*;
import Pieces.PieceLogic.*;

public class Queen extends Piece {

    public Queen(Position startPos, PieceColor c) {
        super("Queen", startPos, c);
    }

    @Override
    public boolean canMove(Position p) {
        if (isTaken() || (Board.getPieceAtPos(p) != null && Board.getPieceAtPos(p).getColor() == getColor())) return false;
        return checkAllStraight(p) || checkAllDiagonal(p);
    }

}
