package fr.iut.robotmineur;

public class AffichageConsole {

    public void afficherMonde(Monde monde) {

        System.out.println("Tour " + monde.getTourActuel());
        System.out.println();


        System.out.print("     ");

        for (int colonne = 0; colonne < 10; colonne++) {
            System.out.print("  " + colonne + "  ");
        }

        System.out.println();

        for (int ligne = 0; ligne < 10; ligne++) {

            // Première ligne du secteur
            System.out.print("   ");

            for (int colonne = 0; colonne < 10; colonne++) {
                System.out.print("+----");
            }

            System.out.println("+");

            // Contenu ligne 1
            System.out.print(" " + ligne + " ");

            for (int colonne = 0; colonne < 10; colonne++) {

                Secteur secteur =
                        monde.getSecteur(
                                new Position(ligne, colonne)
                        );

                System.out.print("|" + contenuLigne1(secteur));
            }

            System.out.println("|");

            // Contenu ligne 2
            System.out.print("   ");

            for (int colonne = 0; colonne < 10; colonne++) {

                Secteur secteur =
                        monde.getSecteur(
                                new Position(ligne, colonne)
                        );

                System.out.print("|" + contenuLigne2(secteur));
            }

            System.out.println("|");
        }

        // Dernière bordure
        System.out.print("   ");

        for (int colonne = 0; colonne < 10; colonne++) {
            System.out.print("+----");
        }

        System.out.println("+");

        System.out.println();

        afficherInfosMines(monde);
        afficherInfosEntrepots(monde);
        afficherInfosRobots(monde);
    }

    private String contenuLigne1(Secteur secteur) {

        if (secteur.estEau()) {
            return "X X ";
        }

        if (secteur.getMine() != null) {
            return "M " + secteur.getMine().getId() + " ";
        }

        if (secteur.getEntrepot() != null) {
            return "E " + secteur.getEntrepot().getId() + " ";
        }

        return "    ";
    }

    private String contenuLigne2(Secteur secteur) {

        if (secteur.estEau()) {
            return "X X ";
        }

        if (secteur.getRobot() != null) {
            return "R " + secteur.getRobot().getId() + " ";
        }

        return "    ";
    }

    public void afficherInfosMines(Monde monde) {

        System.out.println("Mines :");

        for (Mine mine : monde.getMines()) {
            System.out.println(mine);
        }

        System.out.println();
    }

    public void afficherInfosEntrepots(Monde monde) {

        System.out.println("Entrepots :");

        for (Entrepot entrepot : monde.getEntrepots()) {
            System.out.println(entrepot);
        }

        System.out.println();
    }

    public void afficherInfosRobots(Monde monde) {

        System.out.println("Robots :");

        for (Robot robot : monde.getRobots()) {
            System.out.println(robot);
        }

        System.out.println();
    }
}