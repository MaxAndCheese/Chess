package Pieces;
import GameLogic.*;
import Pieces.PieceLogic.*;

public class Knight extends Piece {

    public Knight(Position startPos, PieceColor c) {
        super("Knight", startPos, c);
    }

    @Override
    public boolean canMove(Position p) {
        if (isTaken() || (Board.getPieceAtPos(p) != null && Board.getPieceAtPos(p).getColor() == getColor())) return false;
        Position comparedPos = Position.abs(getPos().subtractPos(p));
        return (comparedPos.getX() == 1 && comparedPos.getY() == 2) || (comparedPos.getX() == 2 && comparedPos.getY() == 1);
    }
}
