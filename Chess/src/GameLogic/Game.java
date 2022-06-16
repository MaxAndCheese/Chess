package GameLogic;
import Pieces.*;
import Pieces.PieceLogic.*;
import java.util.Scanner;

public class Game {

    private static boolean whiteTurn = true;
    private static PieceColor colorWin;

    public static void main(String... args) {
        new Board();
        Board.printBoard();
        while (turn() == EndGameType.NONE) {
            Board.printBoard();
            whiteTurn = !whiteTurn;
        }
        Board.printBoard();
        switch (Board.checkIfCheckmate(colorWin.getOtherColor())) {
            case STALEMATE:
                System.out.println("It's a tie!");
                break;

            case CHECKMATE:
                System.out.println(colorWin.getName() + " wins!");
                break;
        }
    }

    //Returns false if a player has lost
    private static EndGameType turn() {
        Scanner scan = new Scanner(System.in);
        PieceColor color;
        if (whiteTurn) {
            color = PieceColor.WHITE;
        }
        else {
            color = PieceColor.BLACK;
        }
        PieceColor otherColor = color.getOtherColor();
        System.out.println(color.getName() + ", take your turn");
        Piece piece = getPieceToMove(color);

        while (!piece.move()) {
            System.out.println("Would you like to select a different piece?");
            if (scan.nextLine().toLowerCase().substring(0,1).equals("y")) {
                piece = getPieceToMove(color);
            }
        }

        if (piece instanceof Pawn && ((Pawn) piece).canPromote()) {
            System.out.println("Would you like to promote your Pawn");
            if (scan.nextLine().toLowerCase().substring(0,1).equals("y")) {
                Position piecePos = piece.getPos();
                PieceColor pieceColor = piece.getColor();
                Piece[] teamPieces = Board.getTeamPieces(color);
                int indexOfPiece = Board.getIndexOfPiece(piece, color);
                System.out.println("Would you like to promote your Pawn to a Knight, a Bishop, a Rook, or a Queen?");
                switch(scan.nextLine().toLowerCase().substring(0,1)) {
                    case "k":
                        teamPieces[indexOfPiece] = new Knight(piecePos, pieceColor);
                        break;

                    case "b":
                        teamPieces[indexOfPiece] = new Bishop(piecePos, pieceColor);
                        break;

                    case "r":
                        teamPieces[indexOfPiece] = new Rook(piecePos, pieceColor);
                        break;

                    default:
                        teamPieces[indexOfPiece] = new Queen(piecePos, pieceColor);
                        break;
                }
            }
        }

        EndGameType endGame = Board.checkIfCheckmate(otherColor);
        if (endGame == EndGameType.NONE && Board.isThreatenedByColor(Board.getKing(otherColor).getPos(), color)) System.out.println(otherColor.getName() + " is in check");
        else switch (endGame) {
            case STALEMATE:
                System.out.println(otherColor.getName() + " is in stalemate!");
                break;

            case CHECKMATE:
                colorWin = color;
                System.out.println(otherColor.getName() + " is in checkmate!");
                break;
        }
        return endGame;
    }

    private static Piece getPieceToMove(PieceColor color) {
        System.out.println("Enter the position of the piece you would like to move");
        Piece pieceAtPos = Board.getPieceAtPos(getUserInput());
        while (pieceAtPos == null || pieceAtPos.getColor() == color.getOtherColor()) {
            System.out.println("Invalid position, please enter a valid position");
            pieceAtPos = Board.getPieceAtPos(getUserInput());
        }
        return pieceAtPos;
    }

    public static Position getUserInput() {
        Scanner scan = new Scanner(System.in);
        String pos = scan.nextLine();
        int x = pos.charAt(0) - 97;
        int y = Math.abs(Integer.parseInt(pos.substring(1)) - 8);
        return new Position(x,y);
    }
}
