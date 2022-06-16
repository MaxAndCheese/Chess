package GameLogic;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position p) {
        x = p.getX();
        y = p.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object p) {
        return p instanceof Position && x == ((Position) p).getX() && y == ((Position) p).getY();
    }

    public Position addPos(Position p) {
        return new Position(x + p.getX(), y + p.getY());
    }

    public Position subtractPos(Position p) {
        return new Position(x - p.getX(), y - p.getY());
    }

    public Position multiplyPos(Position p) {
        return new Position(x * p.getX(), y * p.getY());
    }

    public static Position abs(Position p) {
        return new Position(Math.abs(p.getX()), Math.abs(p.getY()));
    }

    public Position getChangeFactor(Position p) {
        Position changeFactor = p.subtractPos(this);
        if (changeFactor.getX() > 1) changeFactor.setX(1);
        else if (changeFactor.getX() < -1) changeFactor.setX(-1);
        if (changeFactor.getY() > 1) changeFactor.setY(1);
        else if (changeFactor.getY() < -1) changeFactor.setY(-1);
        return changeFactor;
    }

    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }
}
