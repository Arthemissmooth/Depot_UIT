package fr.iut.robotmineur;

import java.util.List;

public class SimulationAutomatique {

    private final Monde monde;
    private final PlanificateurChemin planificateurChemin;
    private final StrategieRobot strategieRobot;

    public SimulationAutomatique(Monde monde) {
        this.monde = monde;
        this.planificateurChemin = new PlanificateurChemin();
        this.strategieRobot = new StrategieRobot();
    }

    public void jouerTour() {

        for (Robot robot : monde.getRobots()) {
            jouerRobot(robot);
        }

        monde.tourSuivant();
    }

    private void jouerRobot(Robot robot) {

        Secteur secteurActuel =
                monde.getSecteur(robot.getPosition());

        // Récolte automatique
        if (secteurActuel.getMine() != null) {

            boolean recolte =
                    robot.recolter(secteurActuel.getMine());

            if (recolte) {

                System.out.println(
                        "R" + robot.getId()
                                + " récolte du minerai."
                );

                return;
            }
        }

        // Dépôt automatique
        if (secteurActuel.getEntrepot() != null) {

            boolean depot =
                    robot.deposer(secteurActuel.getEntrepot());

            if (depot) {

                System.out.println(
                        "R" + robot.getId()
                                + " dépose son minerai."
                );

                return;
            }
        }

        // Recherche d'objectif
        Position objectif =
                strategieRobot.trouverObjectif(robot, monde);

        if (objectif == null) {

            System.out.println(
                    "R" + robot.getId()
                            + " ne trouve aucun objectif."
            );

            return;
        }

        // Calcul du chemin
        List<Position> chemin =
                planificateurChemin.calculerChemin(
                        monde,
                        robot.getPosition(),
                        objectif
                );

        if (chemin == null || chemin.size() < 2) {

            System.out.println(
                    "R" + robot.getId()
                            + " ne trouve aucun chemin."
            );

            return;
        }

        // Prochaine position
        Position prochainePosition = chemin.get(1);

        Direction direction =
                trouverDirection(
                        robot.getPosition(),
                        prochainePosition
                );

        if (direction != null) {

            boolean deplacement =
                    monde.deplacerRobot(robot, direction);

            if (deplacement) {

                System.out.println(
                        "R" + robot.getId()
                                + " avance vers "
                                + direction
                );

            } else {

                System.out.println(
                        "R" + robot.getId()
                                + " est bloqué."
                );
            }
        }
    }

    private Direction trouverDirection(
            Position depart,
            Position arrivee
    ) {

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

    public boolean simulationTerminee() {

        for (Mine mine : monde.getMines()) {

            if (!mine.estVide()) {
                return false;
            }
        }

        return true;
    }
}