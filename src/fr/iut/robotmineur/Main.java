package fr.iut.robotmineur;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Monde monde = new Monde();

        System.out.println("Bienvenue dans la simulation de robots mineurs");


        System.out.println("Combien de mines ? (2 à 4)");
        int nbMines = Integer.parseInt(scanner.nextLine());

        for (int i = 1; i <= nbMines; i++) {

            System.out.println("Position de la mine " + i + " (ligne colonne) :");
            int ligne = scanner.nextInt();
            int colonne = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Type de la mine " + i + " (1 = OR, 2 = NICKEL) :");
            int type = Integer.parseInt(scanner.nextLine());

            TypeMinerai typeMinerai;
            if (type == 1) {
                typeMinerai = TypeMinerai.OR;
            } else {
                typeMinerai = TypeMinerai.NICKEL;
            }

            System.out.println("Quantité initiale de minerais (entre 50 et 100) :");
            int quantite = Integer.parseInt(scanner.nextLine());

            Mine mine = new Mine(
                    i,
                    new Position(ligne, colonne),
                    typeMinerai,
                    quantite,
                    quantite
            );

            monde.ajouterMine(mine);
        }


        int nbEau;

        do {
            System.out.println("Combien de cases d'eau ? (max 10)");
            nbEau = Integer.parseInt(scanner.nextLine());
        } while (nbEau < 0 || nbEau > 10);


        for (int i = 1; i <= nbEau; i++) {

            System.out.println("Position de l'eau " + i + " (ligne colonne) :");
            int ligne = scanner.nextInt();
            int colonne = scanner.nextInt();
            scanner.nextLine();

            monde.ajouterEau(new Position(ligne, colonne));
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


        System.out.println("Combien de robots ? (1 à 5)");
        int nbRobots = Integer.parseInt(scanner.nextLine());

        for (int i = 1; i <= nbRobots; i++) {

            System.out.println("Nom du robot " + i + " :");
            String nom = scanner.nextLine();

            System.out.println("Type du robot " + i + " (1 = OR, 2 = NICKEL) :");
            int type = Integer.parseInt(scanner.nextLine());

            System.out.println("Position du robot " + i + " (ligne colonne) :");
            int ligne = scanner.nextInt();
            int colonne = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Capacité de stockage du robot " + nom + " (entre 5 et 9) :");
            int capaciteStockage = Integer.parseInt(scanner.nextLine());

            System.out.println("Capacité d'extraction du robot " + nom + " (entre 1 et 3) :");
            int capaciteExtraction = Integer.parseInt(scanner.nextLine());

            Robot robot;

            if (type == 1) {
                robot = new RobotOr(
                        i,
                        new Position(ligne, colonne),
                        capaciteStockage,
                        capaciteExtraction
                );
            } else {
                robot = new RobotNickel(
                        i,
                        new Position(ligne, colonne),
                        capaciteStockage,
                        capaciteExtraction
                );
            }

            monde.ajouterRobot(robot);
        }


        JeuConsole jeu = new JeuConsole(monde);
        jeu.lancer();
    }
}
