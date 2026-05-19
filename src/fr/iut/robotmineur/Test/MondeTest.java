package fr.iut.robotmineur.Test;
import fr.iut.robotmineur.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MondeTest {

    @Test
    void testDeplacementValideTerrainLibre() {
        Monde monde = new Monde();
        Robot robot = new RobotOr(1, "Alpha", new Position(1, 1), 5, 2);

        monde.ajouterRobot(robot);

        boolean resultat = monde.deplacerRobot(robot, Direction.EST);

        assertTrue(resultat);
        assertEquals(1, robot.getPosition().getLigne());
        assertEquals(2, robot.getPosition().getColonne());
    }

    @Test
    void testDeplacementVersEauImpossible() {
        Monde monde = new Monde();
        Robot robot = new RobotOr(1, "Alpha", new Position(1, 1), 5, 2);

        monde.ajouterEau(new Position(1, 2));
        monde.ajouterRobot(robot);

        boolean resultat = monde.deplacerRobot(robot, Direction.EST);

        assertFalse(resultat);
        assertEquals(1, robot.getPosition().getLigne());
        assertEquals(1, robot.getPosition().getColonne());
    }

    @Test
    void testDeplacementVersRobotImpossible() {
        Monde monde = new Monde();

        Robot robot1 = new RobotOr(1, "Alpha", new Position(1, 1), 5, 2);
        Robot robot2 = new RobotNickel(2, "Beta", new Position(1, 2), 5, 2);

        monde.ajouterRobot(robot1);
        monde.ajouterRobot(robot2);

        boolean resultat = monde.deplacerRobot(robot1, Direction.EST);

        assertFalse(resultat);
        assertEquals(1, robot1.getPosition().getLigne());
        assertEquals(1, robot1.getPosition().getColonne());
    }

    @Test
    void testDeplacementHorsGrilleNord() {
        Monde monde = new Monde();
        Robot robot = new RobotOr(1, "Alpha", new Position(0, 0), 5, 2);

        monde.ajouterRobot(robot);

        boolean resultat = monde.deplacerRobot(robot, Direction.NORD);

        assertFalse(resultat);
        assertEquals(0, robot.getPosition().getLigne());
        assertEquals(0, robot.getPosition().getColonne());
    }

    @Test
    void testDeplacementHorsGrilleSud() {
        Monde monde = new Monde();
        Robot robot = new RobotNickel(1, "Beta", new Position(9, 9), 5, 2);

        monde.ajouterRobot(robot);

        boolean resultat = monde.deplacerRobot(robot, Direction.SUD);

        assertFalse(resultat);
        assertEquals(9, robot.getPosition().getLigne());
        assertEquals(9, robot.getPosition().getColonne());
    }
}
