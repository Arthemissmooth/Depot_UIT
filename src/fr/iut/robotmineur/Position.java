package fr.iut.robotmineur;

public class Position {

    private int ligne;
    private int colonne;

    public Position(int ligne, int colonne) {
        this.ligne = ligne;
        this.colonne = colonne;
    }

    public int getLigne() {
        return ligne;
    }

    public int getColonne() {
        return colonne;
    }

    public void setLigne(int ligne) {
        this.ligne = ligne;
    }

    public void setColonne(int colonne) {
        this.colonne = colonne;
    }

    public Position deplacer(Direction direction) {
        if (direction == Direction.NORD) {
            return new Position(ligne - 1, colonne);
        } else if (direction == Direction.SUD) {
            return new Position(ligne + 1, colonne);
        } else if (direction == Direction.EST) {
            return new Position(ligne, colonne + 1);
        } else {
            return new Position(ligne, colonne - 1);
        }
    }

    @Override
    public String toString() {
        return "(" + ligne + "," + colonne + ")";
    }
}