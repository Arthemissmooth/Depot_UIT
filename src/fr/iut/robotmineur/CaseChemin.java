package fr.iut.robotmineur;

public class CaseChemin {

    private Position position;
    private CaseChemin parent;

    private int g;
    private int h;
    private int f;

    public CaseChemin(Position position) {
        this.position = position;
        this.parent = null;
        this.g = 0;
        this.h = 0;
        this.f = 0;
    }

    public Position getPosition() {
        return position;
    }

    public CaseChemin getParent() {
        return parent;
    }

    public void setParent(CaseChemin parent) {
        this.parent = parent;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
        calculerF();
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
        calculerF();
    }

    public int getF() {
        return f;
    }

    private void calculerF() {
        this.f = this.g + this.h;
    }
}