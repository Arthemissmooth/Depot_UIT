package fr.iut.robotmineur;

public class Main {

    public static void main(String[] args) {

        Monde monde = new Monde();

        monde.ajouterEau(new Position(0, 1));
        monde.ajouterEau(new Position(2, 5));
        monde.ajouterEau(new Position(3, 3));
        monde.ajouterEau(new Position(6, 6));
        monde.ajouterEau(new Position(8, 3));

        Mine mineOr =
                new Mine(
                        1,
                        new Position(2, 2),
                        TypeMinerai.OR,
                        80,
                        80
                );

        Mine mineNickel =
                new Mine(
                        2,
                        new Position(6, 5),
                        TypeMinerai.NICKEL,
                        70,
                        70
                );

        Entrepot entrepotOr =
                new Entrepot(
                        1,
                        new Position(0, 0),
                        TypeMinerai.OR
                );

        Entrepot entrepotNickel =
                new Entrepot(
                        2,
                        new Position(9, 9),
                        TypeMinerai.NICKEL
                );

        Robot robotOr =
                new RobotOr(
                        1,
                        new Position(1, 1),
                        5,
                        2
                );

        Robot robotNickel =
                new RobotNickel(
                        2,
                        new Position(4, 4),
                        6,
                        3
                );

        monde.ajouterMine(mineOr);
        monde.ajouterMine(mineNickel);

        monde.ajouterEntrepot(entrepotOr);
        monde.ajouterEntrepot(entrepotNickel);

        monde.ajouterRobot(robotOr);
        monde.ajouterRobot(robotNickel);

        AffichageConsole affichage =
                new AffichageConsole();

        for (int i = 0; i < 10; i++) {

            affichage.afficherMonde(monde);

            monde.tourSuivant();
        }
    }
}