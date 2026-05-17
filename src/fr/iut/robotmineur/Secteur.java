package fr.iut.robotmineur;

public class Secteur {

    private Position position;
    private TypeSecteur typeSecteur;
    private Mine mine;
    private Entrepot entrepot;
    private Robot robot;

    public Secteur(Position position, TypeSecteur typeSecteur) {
        this.position = position;
        this.typeSecteur = typeSecteur;
        this.mine = null;
        this.entrepot = null;
        this.robot = null;
    }

    public Position getPosition() {
        return position;
    }

    public TypeSecteur getTypeSecteur() {
        return typeSecteur;
    }

    public Mine getMine() {
        return mine;
    }

    public Entrepot getEntrepot() {
        return entrepot;
    }

    public Robot getRobot() {
        return robot;
    }

    public boolean estEau() {
        return typeSecteur == TypeSecteur.EAU;
    }

    public boolean estLibrePourRobot() {
        return !estEau() && robot == null;
    }

    public boolean estVideTotalement() {
        return !estEau() && mine == null && entrepot == null && robot == null;
    }

    public boolean estLibrePourMineOuEntrepot() {
        return !estEau() && mine == null && entrepot == null;
    }

    public void placerMine(Mine mine) {
        this.mine = mine;
    }

    public void placerEntrepot(Entrepot entrepot) {
        this.entrepot = entrepot;
    }

    public void placerRobot(Robot robot) {
        this.robot = robot;
    }

    public void retirerRobot() {
        this.robot = null;
    }
}