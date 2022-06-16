package Pieces;
import GameLogic.*;
import Pieces.PieceLogic.*;
import java.util.Scanner;

public class Rook extends Piece implements Castleable {

    private boolean hasMoved = false;

    public Rook(Position startPos, PieceColor c) {
        super("Rook", startPos, c);
    }

    @Override
    public boolean move() {
        Scanner scan = new Scanner(System.in);
        CastleType castleType = canCastle();
        if (castleType != CastleType.NONE) {
            System.out.println("Would you like to castle?");
            String str = scan.nextLine();
            if (str.substring(0, 1).toLowerCase().equals("y")) {
                castle(castleType);
                return true;
            }
        }
        boolean move = super.move();
        if (!hasMoved && move) hasMoved = true;
        return move;
    }

    @Override
    public boolean canMove(Position p) {
        if (isTaken() || (Board.getPieceAtPos(p) != null && Board.getPieceAtPos(p).getColor() == getColor())) return false;
        return checkAllStraight(p);
    }

    @Override
    public void castle(CastleType castleType) {
        Piece[] teamPieces = Board.getTeamPieces(getColor());
        if (castleType == CastleType.KINGSIDE) {
            forceMove(new Position(3, getPos().getY()));
            teamPieces[15].forceMove(new Position(2, getPos().getY()));
        }
        else {
            forceMove(new Position(5, getPos().getY()));
            teamPieces[15].forceMove(new Position(6, getPos().getY()));
        }
    }

    @Override
    public CastleType canCastle() {
        Piece[] teamPieces = Board.getTeamPieces(getColor());
        if (hasMoved || Board.isThreatenedByColor(getPos(), getOpponentColor()) || Board.isThreatenedByColor(teamPieces[15].getPos(), getOpponentColor())) return CastleType.NONE;
        if (!((King) teamPieces[15]).hasMoved()) {
            //Queenside rook
            if (getPos().getX() == 0) {
                for (int i = 1; i < 4; i++) {
                    if (Board.getPieceAtPos(new Position(i,getPos().getY())) != null || Board.isThreatenedByColor(new Position(i,getPos().getY()), getOpponentColor())) return CastleType.NONE;
                }
                return CastleType.QUEENSIDE;
            }
            //Kingside rook
            else {
                for (int i = 5; i < 7; i++) {
                    if (Board.getPieceAtPos(new Position(i,getPos().getY())) != null || Board.isThreatenedByColor(new Position(i,getPos().getY()), getOpponentColor())) return CastleType.NONE;
                }
                return CastleType.KINGSIDE;
            }
        }
        return CastleType.NONE;
    }

    public boolean hasMoved() {
        return hasMoved;
    }
}

