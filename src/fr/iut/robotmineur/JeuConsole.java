package fr.iut.robotmineur;

import java.util.Scanner;

public class JeuConsole {

    private Monde monde;
    private AffichageConsole affichage;
    private Scanner scanner;

    public JeuConsole(Monde monde) {
        this.monde = monde;
        this.affichage = new AffichageConsole();
        this.scanner = new Scanner(System.in);
    }

    public void lancer() {

        boolean continuer = true;

        while (continuer) {

            affichage.afficherMonde(monde);

            for (Robot robot : monde.getRobots()) {
                jouerRobot(robot);
            }

            System.out.println("Voulez-vous continuer ? (oui/non)");
            String reponse = scanner.nextLine();

            if (reponse.equalsIgnoreCase("non")) {
                continuer = false;
            } else {
                monde.tourSuivant();
            }
        }

        System.out.println("Fin de la simulation.");
    }

    private void jouerRobot(Robot robot) {

        System.out.println();
        System.out.println("Robot R" + robot.getId()
                + " (" + robot.getTypeMinerai() + ") à la position "
                + robot.getPosition());

        System.out.println("Choisissez une action :");
        System.out.println("1 - Avancer");
        System.out.println("2 - Récolter");
        System.out.println("3 - Déposer");
        System.out.println("4 - Attendre");

        String choix = scanner.nextLine();

        switch (choix) {
            case "1":
                choisirDirection(robot);
                break;

            case "2":
                recolter(robot);
                break;

            case "3":
                deposer(robot);
                break;

            case "4":
                System.out.println("R" + robot.getId() + " attend.");
                break;

            default:
                System.out.println("Action invalide. Le robot attend.");
                break;
        }
    }

    private void choisirDirection(Robot robot) {

        System.out.println("Direction :");
        System.out.println("1 - Nord");
        System.out.println("2 - Sud");
        System.out.println("3 - Est");
        System.out.println("4 - Ouest");

        String choix = scanner.nextLine();

        Direction direction;

        switch (choix) {
            case "1":
                direction = Direction.NORD;
                break;
            case "2":
                direction = Direction.SUD;
                break;
            case "3":
                direction = Direction.EST;
                break;
            case "4":
                direction = Direction.OUEST;
                break;
            default:
                System.out.println("Direction invalide.");
                return;
        }

        boolean ok = monde.deplacerRobot(robot, direction);

        if (ok) {
            System.out.println("R" + robot.getId() + " avance vers " + direction);
        } else {
            System.out.println("Déplacement impossible.");
        }
    }

    private void recolter(Robot robot) {

        Secteur secteur = monde.getSecteur(robot.getPosition());

        if (secteur.getMine() == null) {
            System.out.println("Il n'y a pas de mine ici.");
            return;
        }

        boolean ok = robot.recolter(secteur.getMine());

        if (ok) {
            System.out.println("R" + robot.getId() + " récolte du minerai.");
        } else {
            System.out.println("Récolte impossible.");
        }
    }

    private void deposer(Robot robot) {

        Secteur secteur = monde.getSecteur(robot.getPosition());

        if (secteur.getEntrepot() == null) {
            System.out.println("Il n'y a pas d'entrepôt ici.");
            return;
        }

        boolean ok = robot.deposer(secteur.getEntrepot());

        if (ok) {
            System.out.println("R" + robot.getId() + " dépose ses minerais.");
        } else {
            System.out.println("Dépôt impossible.");
        }
    }
}





