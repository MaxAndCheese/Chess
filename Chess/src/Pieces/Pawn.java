package Pieces;
import GameLogic.*;
import Pieces.PieceLogic.*;

public class Pawn extends Piece {

    private boolean hasMoved = false;

    @Override
    public boolean move() {
        boolean move = super.move();
        if (!hasMoved && move) hasMoved = true;
        return move;
    }

    public Pawn(Position startPos, PieceColor c) {
        super("Pawn", startPos, c);
    }

    @Override
    public boolean canMove(Position p) {
        Position checkedPos;
        if (isTaken() || (Board.getPieceAtPos(p) != null && Board.getPieceAtPos(p).getColor() == getColor())) return false;
        if (!hasMoved) {
            checkedPos = getPos().addPos(new Position(0, 2).multiplyPos(getColor().getChangeFactor()));
            if (p.equals(checkedPos) && Board.getPieceAtPos(checkedPos) == null)  return true;
        }
        //This is gross, but it works
        //Java is yelling at me saying that some method calls could result in null, but that is not possible, Java doesn't know that though >:(
        checkedPos = getPos().addPos(new Position(0, 1).multiplyPos(getColor().getChangeFactor()));
        if (p.equals(checkedPos) && Board.getPieceAtPos(checkedPos) == null) return true;
        checkedPos = getPos().addPos(new Position(-1, 1).multiplyPos(getColor().getChangeFactor()));
        if (p.equals(checkedPos) && Board.getPieceAtPos(checkedPos) != null && Board.getPieceAtPos(checkedPos).getColor() == getOpponentColor()) return true;
        checkedPos = getPos().addPos(new Position(1, 1).multiplyPos(getColor().getChangeFactor()));
        return p.equals(checkedPos) && Board.getPieceAtPos(checkedPos) != null && Board.getPieceAtPos(checkedPos).getColor() == getOpponentColor();
    }

    public boolean canPromote() {
        if (isTaken()) return false;
        if (getColor() == PieceColor.WHITE && getPos().getY() == 0) return true;
        return getColor() == PieceColor.BLACK && getPos().getY() == 7;
    }
}
