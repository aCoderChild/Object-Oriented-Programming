import java.util.Objects;

public class Bishop extends Piece {
    /**
     * Constructors
     */
    public Bishop(int coordinatesX, int coordinatesY) {
        super(coordinatesX, coordinatesY);
    }

    public Bishop(int coordinatesX, int coordinatesY, String color) {
        super(coordinatesX, coordinatesY, color);
    }

    @Override
    public String getSymbol() {
        return "B";
    }

    @Override
    public boolean canMove(Board board, int x, int y) {
        if (!board.validate(x, y) || isInvalidBishopMove(x, y)) {
            return false;
        }

        Piece pieceAt = board.getAt(x, y);
        if (pieceAt != null) {
            if (Objects.equals(pieceAt.getColor(), this.getColor())) {
                return false;
            }
        }

        int dx = this.getCoordinatesX() < x ? 1 : -1;
        int dy = this.getCoordinatesY() < y ? 1 : -1;

        int currX = this.getCoordinatesX() + dx;
        int currY = this.getCoordinatesY() + dy;

        while (currX != x) {
            if (board.getAt(currX, currY) != null) {
                return false;
            }
            currX += dx;
            currY += dy;
        }

        return true;
    }

    public boolean isInvalidBishopMove(int x, int y) {
        return (Math.abs(this.getCoordinatesX() - x)
                != Math.abs(this.getCoordinatesY() - y)) // not diagonal
                || (this.getCoordinatesX() == x && this.getCoordinatesY() == y); // no move
    }
}