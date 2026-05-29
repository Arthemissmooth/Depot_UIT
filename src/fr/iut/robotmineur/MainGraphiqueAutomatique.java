package fr.iut.robotmineur;

import javax.swing.SwingUtilities;
import java.util.Scanner;

public class MainGraphiqueAutomatique {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Monde monde = new Monde();

        System.out.println("  SIMULATION ROBOTS MINEURS");

        // Nom robot OR
        String nomRobotOr;

        do {

            System.out.println("Nom du robot OR :");

            nomRobotOr = scanner.nextLine();

            if (nomRobotOr.isBlank()) {
                System.out.println("Nom invalide.");
            }

        } while (nomRobotOr.isBlank());

        // Nom robot NICKEL
        String nomRobotNickel;

        do {

            System.out.println("Nom du robot NICKEL :");

            nomRobotNickel = scanner.nextLine();

            if (nomRobotNickel.isBlank()) {
                System.out.println("Nom invalide.");
            }

        } while (nomRobotNickel.isBlank());

        // Initialisation du monde
        monde.initialiserAleatoirement(
                nomRobotOr,
                nomRobotNickel
        );

        // Choix du mode
        int choix;

        do {

            System.out.println();
            System.out.println("Choisissez un mode :");
            System.out.println("1 - Manuel");
            System.out.println("2 - Automatique");

            choix = lireEntier(scanner);

            if (choix != 1 && choix != 2) {
                System.out.println("Choix invalide.");
            }

        } while (choix != 1 && choix != 2);

        int mode = choix;

        // Lancement Swing
        SwingUtilities.invokeLater(() -> {

            FenetreSimulationSwing fenetre =
                    new FenetreSimulationSwing(monde);

            fenetre.setVisible(true);

            // Mode automatique
            if (mode == 2) {

                SimulationAutomatique simulation =
                        new SimulationAutomatique(monde);

                new Thread(() -> {

                    while (!simulation.simulationTerminee()) {

                        simulation.jouerTour();

                        SwingUtilities.invokeLater(
                                fenetre::repaint
                        );

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println(
                            "Simulation automatique terminée."
                    );

                }).start();
            }
        });
    }

    private static int lireEntier(Scanner scanner) {

        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}