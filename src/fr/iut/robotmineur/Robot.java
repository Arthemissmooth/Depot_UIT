package fr.iut.robotmineur;

public abstract class Robot {

    protected int id;
    protected Position position;

    protected int stockActuel;
    protected int capaciteStockage;
    protected int capaciteExtraction;

    public Robot(int id,
                 Position position,
                 int capaciteStockage,
                 int capaciteExtraction) {

        this.id = id;
        this.position = position;

        this.capaciteStockage = capaciteStockage;
        this.capaciteExtraction = capaciteExtraction;

        this.stockActuel = 0;
    }

    public int getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public int getStockActuel() {
        return stockActuel;
    }

    public int getCapaciteStockage() {
        return capaciteStockage;
    }

    public int getCapaciteExtraction() {
        return capaciteExtraction;
    }



    public abstract TypeMinerai getTypeMinerai();

    public boolean estPlein() {
        return stockActuel >= capaciteStockage;
    }

    public boolean estVide() {
        return stockActuel == 0;
    }

    public boolean recolter(Mine mine) {

        if (mine == null) {
            return false;
        }

        if (mine.estVide()) {
            return false;
        }

        if (mine.getTypeMinerai() != getTypeMinerai()) {
            return false;
        }

        if (estPlein()) {
            return false;
        }

        int placeDisponible =
                capaciteStockage - stockActuel;

        int quantiteAExtraire =
                Math.min(capaciteExtraction, placeDisponible);

        int quantiteRecuperee =
                mine.extraire(quantiteAExtraire);

        stockActuel += quantiteRecuperee;

        return true;
    }

    public boolean deposer(Entrepot entrepot) {

        if (entrepot == null) {
            return false;
        }

        if (entrepot.getTypeMinerai() != getTypeMinerai()) {
            return false;
        }

        entrepot.deposer(stockActuel);

        stockActuel = 0;

        return true;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {

        return "R" + id +
                " " + position +
                " " + getTypeMinerai() +
                " " + stockActuel +
                "/" + capaciteStockage;
    }
}