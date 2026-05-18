package fr.iut.robotmineur;

import javax.swing.SwingUtilities;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Monde monde = new Monde();

        System.out.println("Bienvenue dans la simulation de robots mineurs");

        String nomRobotOr;
        do {
            System.out.println("Nom du robot OR :");
            nomRobotOr = scanner.nextLine();

            if (nomRobotOr.isBlank()) {
                System.out.println("Nom invalide.");
            }

        } while (nomRobotOr.isBlank());

        String nomRobotNickel;
        do {
            System.out.println("Nom du robot NICKEL :");
            nomRobotNickel = scanner.nextLine();

            if (nomRobotNickel.isBlank()) {
                System.out.println("Nom invalide.");
            }

        } while (nomRobotNickel.isBlank());

        monde.initialiserAleatoirement(nomRobotOr, nomRobotNickel);

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
}