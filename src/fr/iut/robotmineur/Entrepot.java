package fr.iut.robotmineur;

public class Entrepot {

    private int id;
    private Position position;
    private TypeMinerai typeMinerai;
    private int stock;


    public Entrepot(int id, Position position, TypeMinerai typeMinerai) {
        this.id = id;
        this.position = position;
        this.typeMinerai = typeMinerai;
        this.stock = 0;
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


    public int getStock() {
        return stock;
    }


    public void deposer(int quantite) {
        stock = stock + quantite;
    }


    @Override
    public String toString() {
        return "E" + id + " " + position + " " + typeMinerai + " " + stock;
    }
}




