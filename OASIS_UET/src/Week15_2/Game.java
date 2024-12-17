import java.util.ArrayList;

public class Game {
    private Board board;
    private ArrayList<Move> moveHistory;

    public Game(Board board) {
        this.board = board;
        this.moveHistory = new ArrayList<>();
    }

    public void movePiece(Piece piece, int x, int y) {
        if (piece.canMove(board, x, y)) {
            Piece killedPiece = board.getAt(x, y);
            if (killedPiece != null) {
                board.removeAt(x, y); // Nếu có quân bị ăn, xóa quân đó
            }
            board.removeAt(piece.getCoordinatesX(), piece.getCoordinatesY());
            piece.setCoordinatesX(x);
            piece.setCoordinatesY(y);
            board.addPiece(piece);

            Move move = killedPiece == null ? new Move(piece.getCoordinatesX(), x, piece.getCoordinatesY(), y, piece) :
                    new Move(piece.getCoordinatesX(), x, piece.getCoordinatesY(), y, piece, killedPiece);
            moveHistory.add(move);
        }
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public ArrayList<Move> getMoveHistory() {
        return moveHistory;
    }
}
