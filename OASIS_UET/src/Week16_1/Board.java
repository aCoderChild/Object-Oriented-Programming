import java.util.ArrayList;

public class Board {
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    private ArrayList<Piece> pieces;

    public Board() {
        this.pieces = new ArrayList<>();
    }

    public boolean validate(int x, int y) {
        return x >= 1 && x <= WIDTH && y >= 1 && y <= HEIGHT;
    }

    public void addPiece(Piece piece) {
        if (validate(piece.getCoordinatesX(), piece.getCoordinatesY()) && !isOccupied(piece.getCoordinatesX(), piece.getCoordinatesY())) {
            pieces.add(piece);
        }
    }

    public Piece getAt(int x, int y) {
        for (Piece piece : pieces) {
            if (piece.getCoordinatesX() == x && piece.getCoordinatesY() == y) {
                return piece;
            }
        }
        return null;
    }

    public void removeAt(int x, int y) {
        pieces.removeIf(piece -> piece.getCoordinatesX() == x && piece.getCoordinatesY() == y);
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(ArrayList<Piece> pieces) {
        this.pieces = pieces;
    }

    private boolean isOccupied(int x, int y) {
        return getAt(x, y) != null;
    }
}