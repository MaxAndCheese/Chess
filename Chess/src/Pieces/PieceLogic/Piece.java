package Pieces.PieceLogic;
import GameLogic.*;
import java.util.ArrayList;

public abstract class Piece {

    private String name;
    private PieceColor color;
    private Position pos;
    private boolean taken = false;

    public Piece(String n, Position startPos, PieceColor c) {
        name = n;
        pos = startPos;
        color = c;
    }

    //Returns false if piece is unable to move
    public boolean move() {
        System.out.println("Enter a position to move your " + getName());
        Position p = Game.getUserInput();
        if (canMove(p)) {
            if (isOccupiedByTeam(p, getOpponentColor())) Board.getPieceAtPos(p).takePiece();
            pos = p;
            return true;
        }
        System.out.println("Cannot move to this position");
        return false;
    }

    //The most important method in the entire program, returns true when a piece can move to a position and returns false when it cannot
    public abstract boolean canMove(Position pos);

    public boolean canMoveAnywhere() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (canMove(new Position(j, i))) return true;
            }
        }
        return false;
    }

    //Only use this method when absolutely necessary, as it does not check for overlap or piece movement limitations
    public void forceMove(Position p) {
        pos = p;
    }

    private boolean isOccupiedByTeam(Position p, PieceColor color) {
        Piece piece = Board.getPieceAtPos(p);
        return piece != null && piece.getColor() == color;
    }

    public ArrayList<Position> getThreatenedSpaces() {
        ArrayList<Position> threatenedSpaces = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (canMove(new Position(j,i))) threatenedSpaces.add(new Position(j,i));
            }
        }
        return threatenedSpaces;
    }

    protected boolean checkAllStraight(Position p) {

        Position comparedPos = Position.abs(p.subtractPos(pos));
        if (comparedPos.getX() != 0 && comparedPos.getY() != 0) return false;

        Position changeFactor = pos.getChangeFactor(p);
        Position checkedPos = pos;

        for (int i = 0; i < comparedPos.getX() + comparedPos.getY() - 1; i++) {
            checkedPos = checkedPos.addPos(changeFactor);
            if (Board.getPieceAtPos(checkedPos) != null) return false;
        }
        return true;
    }

    protected boolean checkAllDiagonal(Position p) {

        Position comparedPos = Position.abs(p.subtractPos(pos));
        if (comparedPos.getX() != comparedPos.getY()) return false;

        Position changeFactor = pos.getChangeFactor(p);
        Position checkedPos = pos;


        for (int i = 0; i < comparedPos.getX() - 1; i++) {
            checkedPos = checkedPos.addPos(changeFactor);
            if (Board.getPieceAtPos(checkedPos) != null) return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object piece) {
        return piece instanceof Piece && name.equals(((Piece) piece).name) && color == ((Piece) piece).color && pos.equals(((Piece) piece).pos) && taken == ((Piece) piece).taken;
    }

    private String getName() {
        return name;
    }

    public PieceColor getColor() {
        return color;
    }

    public PieceColor getOpponentColor() {
        if (color == PieceColor.WHITE) return PieceColor.BLACK;
        else return PieceColor.WHITE;
    }

    public Position getPos() {
        return pos;
    }

    public boolean isTaken() {
        return taken;
    }

    public void takePiece() {
        taken = true;
    }

    public void untakePiece() {
        taken = false;
    }
}
