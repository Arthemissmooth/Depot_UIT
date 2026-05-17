package fr.iut.robotmineur;

import javax.swing.SwingUtilities;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Monde monde = new Monde();

        System.out.println("Bienvenue dans la simulation de robots mineurs");

        int nbMines;
        do {
            System.out.println("Combien de mines ? (2 à 4)");
            nbMines = lireEntier(scanner);

            if (nbMines < 2 || nbMines > 4) {
                System.out.println("Valeur invalide.");
            }

        } while (nbMines < 2 || nbMines > 4);

        for (int i = 1; i <= nbMines; i++) {

            Position positionMine;
            do {
                positionMine = lirePosition(scanner, "Position de la mine " + i + " :");

                if (!monde.positionDisponiblePourMineOuEntrepot(positionMine)) {
                    System.out.println("Position déjà occupée ou invalide.");
                }

            } while (!monde.positionDisponiblePourMineOuEntrepot(positionMine));

            int type;
            do {
                System.out.println("Type (1 = OR, 2 = NICKEL)");
                type = lireEntier(scanner);

                if (type != 1 && type != 2) {
                    System.out.println("Type invalide.");
                }

            } while (type != 1 && type != 2);

            TypeMinerai typeMinerai = (type == 1) ? TypeMinerai.OR : TypeMinerai.NICKEL;

            int quantite;
            do {
                System.out.println("Quantité (50 à 100)");
                quantite = lireEntier(scanner);

                if (quantite < 50 || quantite > 100) {
                    System.out.println("Quantité invalide.");
                }

            } while (quantite < 50 || quantite > 100);

            Mine mine = new Mine(i, positionMine, typeMinerai, quantite, quantite);
            monde.ajouterMine(mine);
        }

        int nbEau;
        do {
            System.out.println("Combien de cases d'eau ? (max 10)");
            nbEau = lireEntier(scanner);

            if (nbEau < 0 || nbEau > 10) {
                System.out.println("Valeur invalide.");
            }

        } while (nbEau < 0 || nbEau > 10);

        for (int i = 1; i <= nbEau; i++) {

            Position positionEau;
            do {
                positionEau = lirePosition(scanner, "Position de l'eau " + i + " :");

                if (!monde.positionDisponiblePourEau(positionEau)) {
                    System.out.println("Position déjà occupée ou invalide.");
                }

            } while (!monde.positionDisponiblePourEau(positionEau));

            monde.ajouterEau(positionEau);
        }

        Entrepot entrepotOr = new Entrepot(
                1,
                new Position(0, 0),
                TypeMinerai.OR
        );

        Entrepot entrepotNickel = new Entrepot(
                2,
                new Position(9, 9),
                TypeMinerai.NICKEL
        );

        monde.ajouterEntrepot(entrepotOr);
        monde.ajouterEntrepot(entrepotNickel);

        int nbRobots;
        do {
            System.out.println("Combien de robots ? (1 à 5)");
            nbRobots = lireEntier(scanner);

            if (nbRobots < 1 || nbRobots > 5) {
                System.out.println("Valeur invalide.");
            }

        } while (nbRobots < 1 || nbRobots > 5);

        for (int i = 1; i <= nbRobots; i++) {

            String nom;
            do {
                System.out.println("Nom du robot " + i + " :");
                nom = scanner.nextLine();

                if (nom.isBlank()) {
                    System.out.println("Nom invalide.");
                }

            } while (nom.isBlank());

            int type;
            do {
                System.out.println("Type (1 = OR, 2 = NICKEL)");
                type = lireEntier(scanner);

                if (type != 1 && type != 2) {
                    System.out.println("Type invalide.");
                }

            } while (type != 1 && type != 2);

            Position positionRobot;
            do {
                positionRobot = lirePosition(scanner, "Position robot :");

                if (!monde.positionDisponiblePourRobot(positionRobot)) {
                    System.out.println("Position invalide : eau ou robot déjà présent.");
                }

            } while (!monde.positionDisponiblePourRobot(positionRobot));

            int capaciteStockage;
            do {
                System.out.println("Capacité stockage (5 à 9)");
                capaciteStockage = lireEntier(scanner);

                if (capaciteStockage < 5 || capaciteStockage > 9) {
                    System.out.println("Valeur invalide.");
                }

            } while (capaciteStockage < 5 || capaciteStockage > 9);

            int capaciteExtraction;
            do {
                System.out.println("Capacité extraction (1 à 3)");
                capaciteExtraction = lireEntier(scanner);

                if (capaciteExtraction < 1 || capaciteExtraction > 3) {
                    System.out.println("Valeur invalide.");
                }

            } while (capaciteExtraction < 1 || capaciteExtraction > 3);

            Robot robot;

            if (type == 1) {
                robot = new RobotOr(i, positionRobot, capaciteStockage, capaciteExtraction);
            } else {
                robot = new RobotNickel(i, positionRobot, capaciteStockage, capaciteExtraction);
            }

            monde.ajouterRobot(robot);
        }

        int choix;
        do {
            System.out.println("Mode ? 1 = Console, 2 = Graphique");
            choix = lireEntier(scanner);

            if (choix != 1 && choix != 2) {
                System.out.println("Valeur invalide.");
            }

        } while (choix != 1 && choix != 2);

        if (choix == 1) {
            JeuConsole jeu = new JeuConsole(monde);
            jeu.lancer();
        } else {
            SwingUtilities.invokeLater(() -> {
                new FenetreSimulationSwing(monde).setVisible(true);
            });
        }
    }

    private static int lireEntier(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static Position lirePosition(Scanner scanner, String message) {

        int ligne;
        int colonne;

        do {
            System.out.println(message);

            System.out.print("Ligne (0 à 9) : ");
            ligne = lireEntier(scanner);

            System.out.print("Colonne (0 à 9) : ");
            colonne = lireEntier(scanner);

            if (ligne < 0 || ligne > 9 || colonne < 0 || colonne > 9) {
                System.out.println("Position invalide.");
            }

        } while (ligne < 0 || ligne > 9 || colonne < 0 || colonne > 9);

        return new Position(ligne, colonne);
    }
}