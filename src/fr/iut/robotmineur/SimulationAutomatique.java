package fr.iut.robotmineur;

import java.util.List;

public class SimulationAutomatique {

    private Monde monde;
    private PlanificateurChemin planificateurChemin;
    private StrategieRobot strategieRobot;

    public SimulationAutomatique(Monde monde) {
        this.monde = monde;
        this.planificateurChemin = new PlanificateurChemin();
        this.strategieRobot = new StrategieRobot();
    }

    public void jouerTour() {

        if (estTerminee()) {
            return;
        }

        for (Robot robot : monde.getRobots()) {
            jouerRobot(robot);
        }

        monde.tourSuivant();
    }

    private void jouerRobot(Robot robot) {

        Secteur secteurActuel = monde.getSecteur(robot.getPosition());

        if (secteurActuel.getMine() != null && !robot.estPlein()) {
            boolean recolte = robot.recolter(secteurActuel.getMine());

            if (recolte) {
                return;
            }
        }

        if (secteurActuel.getEntrepot() != null && !robot.estVide()) {
            boolean depot = robot.deposer(secteurActuel.getEntrepot());

            if (depot) {
                return;
            }
        }

        Position objectif = strategieRobot.trouverObjectif(robot, monde);

        if (objectif == null) {
            return;
        }

        List<Position> chemin = planificateurChemin.calculerChemin(
                monde,
                robot.getPosition(),
                objectif
        );

        if (chemin.size() < 2) {
            return;
        }

        Position prochainePosition = chemin.get(1);
        Direction direction = trouverDirection(robot.getPosition(), prochainePosition);

        if (direction != null) {
            monde.deplacerRobot(robot, direction);
        }
    }

    public boolean estTerminee() {

        for (Mine mine : monde.getMines()) {
            if (!mine.estVide()) {
                return false;
            }
        }

        for (Robot robot : monde.getRobots()) {
            if (!robot.estVide()) {
                return false;
            }
        }

        return true;
    }

    private Direction trouverDirection(Position depart, Position arrivee) {

        if (arrivee.getLigne() == depart.getLigne() - 1) {
            return Direction.NORD;
        }

        if (arrivee.getLigne() == depart.getLigne() + 1) {
            return Direction.SUD;
        }

        if (arrivee.getColonne() == depart.getColonne() + 1) {
            return Direction.EST;
        }

        if (arrivee.getColonne() == depart.getColonne() - 1) {
            return Direction.OUEST;
        }

        return null;
    }
}
