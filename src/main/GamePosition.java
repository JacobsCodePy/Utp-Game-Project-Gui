package main;

public record GamePosition(int row, int col) {

    public boolean equals(Object obj) {
        if (obj instanceof GamePosition p) {
            return p.row() == row && p.col() == col;
        } else return false;
    }

    public int hashCode() {
        return row * 8 + col;
    }

    public int getIndex(int size) {
        return row() * size + col();
    }

}
