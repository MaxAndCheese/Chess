package Pieces.PieceLogic;
import GameLogic.*;

public enum PieceColor {
    WHITE("White", new Position(1,-1)),
    BLACK("Black", new Position(1,1));

    private String name;
    private Position changeFactor;

    PieceColor(String n, Position c) {
        name = n;
        changeFactor = c;
    }

    public String getName() {
        return name;
    }

    public Position getChangeFactor() {
        return changeFactor;
    }

    public PieceColor getOtherColor() {
        if (this == PieceColor.WHITE) return PieceColor.BLACK;
        else return PieceColor.WHITE;
    }
 }
