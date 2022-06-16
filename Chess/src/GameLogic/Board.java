package GameLogic;
import Pieces.*;
import Pieces.PieceLogic.EndGameType;
import Pieces.PieceLogic.Piece;
import Pieces.PieceLogic.PieceColor;
import java.util.ArrayList;

public class Board {

    private static Piece[] whitePieces = new Piece[16];
    private static Piece[] blackPieces = new Piece[16];

    public Board() {
        //Setup pawns
        for (int i = 0; i < 8; i++) {
            whitePieces[i] = new Pawn(new Position(i, 6), PieceColor.WHITE);
            blackPieces[i] = new Pawn(new Position(i, 1), PieceColor.BLACK);
        }

        //Setup rest of white pieces, I really wish there was a better way to do this
        whitePieces[8] = new Rook(new Position(0,7), PieceColor.WHITE);
        whitePieces[9] = new Rook(new Position(7,7), PieceColor.WHITE);
        whitePieces[10] = new Knight(new Position(1,7), PieceColor.WHITE);
        whitePieces[11] = new Knight(new Position(6,7), PieceColor.WHITE);
        whitePieces[12] = new Bishop(new Position(2,7), PieceColor.WHITE);
        whitePieces[13] = new Bishop(new Position(5,7), PieceColor.WHITE);
        whitePieces[14] = new Queen(new Position(3,7), PieceColor.WHITE);
        whitePieces[15] = new King(new Position(4,7), PieceColor.WHITE);

        //Setup rest of black pieces
        blackPieces[8] = new Rook(new Position(0,0), PieceColor.BLACK);
        blackPieces[9] = new Rook(new Position(7,0), PieceColor.BLACK);
        blackPieces[10] = new Knight(new Position(1,0), PieceColor.BLACK);
        blackPieces[11] = new Knight(new Position(6,0), PieceColor.BLACK);
        blackPieces[12] = new Bishop(new Position(2,0), PieceColor.BLACK);
        blackPieces[13] = new Bishop(new Position(5,0), PieceColor.BLACK);
        blackPieces[14] = new Queen(new Position(3,0), PieceColor.BLACK);
        blackPieces[15] = new King(new Position(4,0), PieceColor.BLACK);
    }

    public static void printBoard() {
        boolean whiteSpace = true;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (j == 0) System.out.print(Math.abs(i - 8) + "\t");
                if (whiteSpace) System.out.print("\u001B[47m");
                else System.out.print("\u001B[100m");
                Piece piece = Board.getPieceAtPos(new Position(j,i));
                if (piece == null || piece.isTaken()) System.out.print("   ");
                else {
                    if (piece.getColor() == PieceColor.WHITE) System.out.print("\u001B[30m");
                    else System.out.print("\u001B[97m");

                    if (piece instanceof Pawn) System.out.print(" P ");
                    else if (piece instanceof Rook) System.out.print(" R ");
                    else if (piece instanceof Knight) System.out.print(" H "); //Since King also starts with K, the knight will use H for Horse
                    else if (piece instanceof Bishop) System.out.print(" B ");
                    else if (piece instanceof Queen) System.out.print(" Q ");
                    else System.out.print(" K ");
                }
                System.out.print("\u001B[0m");
                whiteSpace = !whiteSpace;
            }
            whiteSpace = !whiteSpace;
            System.out.println();
        }
        System.out.print("- \t");
        for (int i = 0; i < 8; i++) {
            char letter = (char)(i + 97);
            System.out.print(" " + letter + " ");
        }
        System.out.println();
        System.out.println();
     }

    public static EndGameType checkIfCheckmate(PieceColor color) {
        Piece[] teamPieces = getTeamPieces(color);
        Piece king = teamPieces[15];
        if (king.isTaken()) return EndGameType.CHECKMATE;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position pos = new Position(j,i);
                if (king.canMove(pos) && !isThreatenedByColor(pos, king.getOpponentColor())) return EndGameType.NONE;
                //Check for possible blocking if king is threatened
                //So. Many. Nested. For Loops.
                if (isThreatenedByColor(king.getPos(), king.getOpponentColor())) {
                    for (Piece piece : teamPieces) {
                        if (piece.canMove(pos)) {
                            Position prevPosition = piece.getPos();
                            Piece pieceAtPos = Board.getPieceAtPos(pos);
                            if (pieceAtPos != null) pieceAtPos.takePiece();
                            piece.forceMove(pos);
                            if (!isThreatenedByColor(king.getPos(), king.getOpponentColor())) {
                                piece.forceMove(prevPosition);
                                if (pieceAtPos != null) pieceAtPos.untakePiece();
                                return EndGameType.NONE;
                            }
                            piece.forceMove(prevPosition);
                            if (pieceAtPos != null) pieceAtPos.untakePiece();
                        }
                    }
                }
            }
        }

        if (isThreatenedByColor(king.getPos(), king.getOpponentColor())) return EndGameType.CHECKMATE;
        else {
            for (int i = 0; i < 15; i++) {
                if (teamPieces[i].canMoveAnywhere()) return EndGameType.NONE;
            }
            return EndGameType.STALEMATE;
        }
    }

    public static boolean isThreatenedByColor(Position p, PieceColor color) {
        ArrayList<Position> colorThreatenedSpaces = Board.getTeamThreatenedSpaces(color);
        for (Position threatenedPos : colorThreatenedSpaces) {
            if (threatenedPos.equals(p)) return true;
        }
        return false;
    }

    public static Piece getPieceAtPos(Position p) {
        for (Piece piece : whitePieces) {
            if (piece.getPos().equals(p)) return piece;
        }
        for (Piece piece : blackPieces) {
            if (piece.getPos().equals(p)) return piece;
        }
        return null;
    }

    private static ArrayList<Position> getTeamThreatenedSpaces(PieceColor color) {
        Piece[] teamPieces = getTeamPieces(color);
        ArrayList<Position> threatenedSpaces = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            threatenedSpaces.addAll(teamPieces[i].getThreatenedSpaces());
        }
        return threatenedSpaces;
    }

    public static int getIndexOfPiece(Piece piece, PieceColor color) {
        Piece[] teamPieces = getTeamPieces(color);
        for (int i = 0; i < 16; i++) {
            if (teamPieces[i].equals(piece)) return i;
        }
        return -1;
    }

    public static Piece getKing(PieceColor color) {
        return getTeamPieces(color)[15];
    }

    public static Piece[] getTeamPieces(PieceColor color) {
        if (color == PieceColor.WHITE) return whitePieces;
        else return blackPieces;
    }

}
