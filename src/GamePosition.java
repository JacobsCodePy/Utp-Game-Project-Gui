public record GamePosition(int row, int col) {

    public boolean equals(Object obj) {
        if (obj instanceof GamePosition p) {
            return p.row() == row && p.col() == col;
        } else return false;
    }

    public int getIndex(int size) {
        return row() * size + col();
    }

}
