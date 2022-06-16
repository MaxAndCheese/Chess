package Pieces;
import GameLogic.*;
import Pieces.PieceLogic.*;
import java.util.ArrayList;
import java.util.Scanner;

public class King extends Piece implements Castleable {

    private boolean hasMoved = false;
    private static Scanner scan = new Scanner(System.in);

    public King(Position startPos, PieceColor c) {
        super("King", startPos, c);
    }

    @Override
    public boolean move() {
        CastleType castleType = canCastle();
        if (castleType != CastleType.NONE) {
            System.out.println("Would you like to castle?");
            String str = scan.nextLine();
            if (str.substring(0,1).toLowerCase().equals("y")) {
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
        Position comparedPos = p.subtractPos(getPos());
        return Math.abs(comparedPos.getX()) < 2 && Math.abs(comparedPos.getY()) < 2 && !getPos().equals(p) && !Board.isThreatenedByColor(p, getOpponentColor());
    }

    //Only used to prevent infinite recursion, does not check for threatened spaces
    private boolean forcedCanMove(Position p) {
        if (isTaken() || (Board.getPieceAtPos(p) != null && Board.getPieceAtPos(p).getColor() == getColor())) return false;
        Position comparedPos = p.subtractPos(getPos());
        return Math.abs(comparedPos.getX()) < 2 && Math.abs(comparedPos.getY()) < 2 && !getPos().equals(p);
    }

    //Override of getThreatenedSpaces to use forcedCanMove instead of canMove to prevent infinite recursion
    @Override
    public ArrayList<Position> getThreatenedSpaces() {
        ArrayList<Position> threatenedSpaces = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (forcedCanMove(new Position(j,i))) threatenedSpaces.add(new Position(j,i));
            }
        }
        return threatenedSpaces;
    }

    @Override
    public void castle(CastleType castleType) {
        Piece[] teamPieces = Board.getTeamPieces(getColor());
        if (castleType == CastleType.BOTH) {
            System.out.println("Would you like to kingside or queenside castle?");
            if (scan.nextLine().substring(0, 1).toLowerCase().equals("k")) castleType = CastleType.KINGSIDE;
            else castleType = CastleType.QUEENSIDE;
        }
        if (castleType == CastleType.KINGSIDE) {
            teamPieces[9].forceMove(new Position(5, getPos().getY()));
            forceMove(new Position(6, getPos().getY()));
        }
        else {
            teamPieces[8].forceMove(new Position(3, getPos().getY()));
            forceMove(new Position(2, getPos().getY()));
        }
    }

    @Override
    public CastleType canCastle() {
        if (hasMoved || Board.isThreatenedByColor(getPos(), getOpponentColor())) return CastleType.NONE;
        Piece[] teamPieces = Board.getTeamPieces(getColor());
        boolean queenside = false;
        boolean kingside = false;
        //Queenside Rook
        if (!((Rook) teamPieces[8]).hasMoved() && !Board.isThreatenedByColor(teamPieces[8].getPos(), getOpponentColor())) {
            queenside = true;
            for (int i = 1; i < 4; i++) {
                if (Board.getPieceAtPos(new Position(i, getPos().getY())) != null || Board.isThreatenedByColor(new Position(i,getPos().getY()), getOpponentColor())) queenside = false;
            }
        }
        //Kingside rook
        if (!((Rook) teamPieces[9]).hasMoved() && !Board.isThreatenedByColor(teamPieces[9].getPos(), getOpponentColor())) {
            kingside = true;
            for (int i = 5; i < 7; i++) {
                if (Board.getPieceAtPos(new Position(i,getPos().getY())) != null || Board.isThreatenedByColor(new Position(i,getPos().getY()), getOpponentColor())) kingside = false;
            }
        }
        if (kingside && queenside) return CastleType.BOTH;
        if (kingside) return CastleType.KINGSIDE;
        if (queenside) return CastleType.QUEENSIDE;
        return CastleType.NONE;
    }

    public boolean hasMoved() {
        return hasMoved;
    }
}
