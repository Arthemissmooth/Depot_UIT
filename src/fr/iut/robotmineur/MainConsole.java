package fr.iut.robotmineur;

import java.util.Scanner;

public class MainConsole {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Monde monde = new Monde();

        System.out.println("Bienvenue dans la simulation CONSOLE");

        System.out.println("Nom du robot OR :");
        String nomRobotOr = scanner.nextLine();

        System.out.println("Nom du robot NICKEL :");
        String nomRobotNickel = scanner.nextLine();

        monde.initialiserAleatoirement(nomRobotOr, nomRobotNickel);

        JeuConsole jeu = new JeuConsole(monde);
        jeu.lancer();
    }
}