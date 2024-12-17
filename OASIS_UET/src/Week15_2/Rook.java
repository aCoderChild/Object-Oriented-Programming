public class Rook extends Piece {

    public Rook(int x, int y) {
        super(x, y);
    }

    public Rook(int x, int y, String color) {
        super(x, y, color);
    }

    @Override
    public String getSymbol() {
        return "R"; // Ký hiệu quốc tế của quân Xe
    }

    @Override
    public boolean canMove(Board board, int x, int y) {
        if (x != getCoordinatesX() && y != getCoordinatesY()) {
            return false; // Xe chỉ di chuyển thẳng hoặc ngang
        }

        int xDirection = Integer.signum(x - getCoordinatesX());
        int yDirection = Integer.signum(y - getCoordinatesY());
        int currentX = getCoordinatesX();
        int currentY = getCoordinatesY();

        // Di chuyển theo chiều ngang hoặc dọc
        while (currentX != x || currentY != y) {
            if (currentX != x) {
                currentX += xDirection;
            } else if (currentY != y) {
                currentY += yDirection;
            }

            Piece pieceAtCurrentPosition = board.getAt(currentX, currentY);
            if (pieceAtCurrentPosition != null) {
                if (!pieceAtCurrentPosition.getColor().equals(getColor())) {
                    return true; // Có quân đối phương, có thể ăn quân
                } else {
                    return false; // Có quân đồng minh cản đường
                }
            }
        }
        return true; // Đường đi không có chướng ngại vật
    }
}
