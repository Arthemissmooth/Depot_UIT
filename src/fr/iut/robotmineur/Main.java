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
            try {
                nbMines = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                nbMines = -1;
            }

            if (nbMines < 2 || nbMines > 4) {
                System.out.println("Valeur invalide.");
            }

        } while (nbMines < 2 || nbMines > 4);

        for (int i = 1; i <= nbMines; i++) {

            int ligne;
            int colonne;

            do {
                System.out.println("Position de la mine " + i + " :");

                try {
                    System.out.print("Ligne (0 à 9) : ");
                    ligne = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    ligne = -1;
                }

                try {
                    System.out.print("Colonne (0 à 9) : ");
                    colonne = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    colonne = -1;
                }

                if (ligne < 0 || ligne > 9 || colonne < 0 || colonne > 9) {
                    System.out.println("Position invalide.");
                }

            } while (ligne < 0 || ligne > 9 || colonne < 0 || colonne > 9);

            int type;

            do {
                System.out.println("Type (1 = OR, 2 = NICKEL)");
                try {
                    type = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    type = -1;
                }

                if (type != 1 && type != 2) {
                    System.out.println("Type invalide.");
                }

            } while (type != 1 && type != 2);

            TypeMinerai typeMinerai = (type == 1) ? TypeMinerai.OR : TypeMinerai.NICKEL;

            int quantite;

            do {
                System.out.println("Quantité (50 à 100)");
                try {
                    quantite = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    quantite = -1;
                }

                if (quantite < 50 || quantite > 100) {
                    System.out.println("Quantité invalide.");
                }

            } while (quantite < 50 || quantite > 100);

            Mine mine = new Mine(i, new Position(ligne, colonne), typeMinerai, quantite, quantite);
            monde.ajouterMine(mine);
        }

        int nbEau;

        do {
            System.out.println("Combien de cases d'eau ? (max 10)");
            try {
                nbEau = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                nbEau = -1;
            }

            if (nbEau < 0 || nbEau > 10) {
                System.out.println("Valeur invalide.");
            }

        } while (nbEau < 0 || nbEau > 10);

        for (int i = 1; i <= nbEau; i++) {

            int ligne;
            int colonne;

            do {
                System.out.println("Position de l'eau " + i + " :");

                try {
                    System.out.print("Ligne (0 à 9) : ");
                    ligne = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    ligne = -1;
                }

                try {
                    System.out.print("Colonne (0 à 9) : ");
                    colonne = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    colonne = -1;
                }

                if (ligne < 0 || ligne > 9 || colonne < 0 || colonne > 9) {
                    System.out.println("Position invalide.");
                }

            } while (ligne < 0 || ligne > 9 || colonne < 0 || colonne > 9);

            monde.ajouterEau(new Position(ligne, colonne));
        }

        Entrepot entrepotOr = new Entrepot(1, new Position(0, 0), TypeMinerai.OR);
        Entrepot entrepotNickel = new Entrepot(2, new Position(9, 9), TypeMinerai.NICKEL);

        monde.ajouterEntrepot(entrepotOr);
        monde.ajouterEntrepot(entrepotNickel);

        int nbRobots;

        do {
            System.out.println("Combien de robots ? (1 à 5)");
            try {
                nbRobots = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                nbRobots = -1;
            }

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
                try {
                    type = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    type = -1;
                }

                if (type != 1 && type != 2) {
                    System.out.println("Type invalide.");
                }

            } while (type != 1 && type != 2);

            int ligne;
            int colonne;

            do {
                System.out.println("Position robot :");

                try {
                    System.out.print("Ligne (0 à 9) : ");
                    ligne = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    ligne = -1;
                }

                try {
                    System.out.print("Colonne (0 à 9) : ");
                    colonne = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    colonne = -1;
                }

                if (ligne < 0 || ligne > 9 || colonne < 0 || colonne > 9) {
                    System.out.println("Position invalide.");
                }

            } while (ligne < 0 || ligne > 9 || colonne < 0 || colonne > 9);

            int capaciteStockage;

            do {
                System.out.println("Capacité stockage (5 à 9)");
                try {
                    capaciteStockage = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    capaciteStockage = -1;
                }

                if (capaciteStockage < 5 || capaciteStockage > 9) {
                    System.out.println("Valeur invalide.");
                }

            } while (capaciteStockage < 5 || capaciteStockage > 9);

            int capaciteExtraction;

            do {
                System.out.println("Capacité extraction (1 à 3)");
                try {
                    capaciteExtraction = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    capaciteExtraction = -1;
                }

                if (capaciteExtraction < 1 || capaciteExtraction > 3) {
                    System.out.println("Valeur invalide.");
                }

            } while (capaciteExtraction < 1 || capaciteExtraction > 3);

            Robot robot;

            if (type == 1) {
                robot = new RobotOr(i, new Position(ligne, colonne), capaciteStockage, capaciteExtraction);
            } else {
                robot = new RobotNickel(i, new Position(ligne, colonne), capaciteStockage, capaciteExtraction);
            }

            monde.ajouterRobot(robot);
        }

        int choix;

        do {
            System.out.println("Mode ? 1 = Console, 2 = Graphique");
            try {
                choix = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choix = -1;
            }

            if (choix < 1 || choix > 2) {
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
}