package fr.iut.robotmineur;

import java.util.ArrayList;
import java.util.List;


    public class Monde {


        private Secteur[][] secteurs;
        private List<Robot> robots;
        private List<Mine> mines;
        private List<Entrepot> entrepots;
        private int tourActuel;
        secteurs[ligne][colonne] = new Secteur(position, TypeSecteur.TERRAIN);

public Secteur getSecteur(Position position) {
    return secteurs[position.getLigne()][position.getColonne()];
}


public boolean positionValide(Position position) {
    return position.getLigne() >= 0
            && position.getLigne() < 10
            && position.getColonne() >= 0
            && position.getColonne() < 10;
}


public void ajouterRobot(Robot robot) {
    robots.add(robot);
    getSecteur(robot.getPosition()).placerRobot(robot);
}


public void ajouterEau(Position position) {
    secteurs[position.getLigne()][position.getColonne()] =
            new Secteur(position, TypeSecteur.EAU);
}


public void ajouterMine(Mine mine) {
    mines.add(mine);
    getSecteur(mine.getPosition()).placerMine(mine);
}


public void ajouterEntrepot(Entrepot entrepot) {
    entrepots.add(entrepot);
    getSecteur(entrepot.getPosition()).placerEntrepot(entrepot);
}


public boolean deplacerRobot(Robot robot, Direction direction) {
    Position anciennePosition = robot.getPosition();
    Position nouvellePosition = anciennePosition.deplacer(direction);


    if (!positionValide(nouvellePosition)) {
        return false;
    }


    Secteur secteurDestination = getSecteur(nouvellePosition);


    if (!secteurDestination.estLibre()) {
        return false;
    }


    getSecteur(anciennePosition).retirerRobot();
    robot.setPosition(nouvellePosition);
    secteurDestination.placerRobot(robot);


    return true;
}


public void tourSuivant() {
    tourActuel++;
}


public List<Robot> getRobots() {
    return robots;
}


public List<Mine> getMines() {
    return mines;
}


public List<Entrepot> getEntrepots() {
    return entrepots;
}


public int getTourActuel() {
    return tourActuel;
}

}



