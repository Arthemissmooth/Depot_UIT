package fr.iut.robotmineur;

public class Mine {

    private int id;
    private Position position;
    private TypeMinerai typeMinerai;
    private int quantiteActuelle;
    private int quantiteInitiale;

    public Mine(int id, Position position, TypeMinerai typeMinerai,
                int quantiteActuelle, int quantiteInitiale) {

        this.id = id;
        this.position = position;
        this.typeMinerai = typeMinerai;
        this.quantiteActuelle = quantiteActuelle;
        this.quantiteInitiale = quantiteInitiale ;
    }

    public int getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public TypeMinerai getTypeMinerai() {
        return typeMinerai;
    }

    public int getQuantiteActuelle() {
        return quantiteActuelle;
    }

    public int getQuantiteInitiale() {
        return quantiteInitiale;
    }

    public boolean estVide() {
        return quantiteActuelle <= 0;
    }

    public int extraire(int quantite) {

        if (quantiteActuelle >= quantite) {
            quantiteActuelle -= quantite;
            return quantite;
        }

        int reste = quantiteActuelle;
        quantiteActuelle = 0;

        return reste;
    }

    public int getMineraiRestant() {
        return quantiteActuelle;
    }


    @Override
    public String toString() {
        return "M" + id +
                " " + position +
                " " + typeMinerai +
                " " + quantiteActuelle +
                "/" + quantiteInitiale;
    }
}