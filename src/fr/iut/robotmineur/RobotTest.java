package fr.iut.robotmineur;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RobotTest {

    @Test
    void testRecolterMineMemeType() {
        Robot robot = new RobotOr(1, "Alpha", new Position(0, 0), 5, 2);
        Mine mine = new Mine(1, new Position(0, 0), TypeMinerai.OR, 80, 80);

        boolean resultat = robot.recolter(mine);

        assertTrue(resultat);
        assertEquals(2, robot.getStockActuel());
        assertEquals(78, mine.getQuantiteActuelle());
    }

    @Test
    void testRecolterMauvaisType() {
        Robot robot = new RobotOr(1, "Alpha", new Position(0, 0), 5, 2);
        Mine mine = new Mine(1, new Position(0, 0), TypeMinerai.NICKEL, 80, 80);

        boolean resultat = robot.recolter(mine);

        assertFalse(resultat);
        assertEquals(0, robot.getStockActuel());
        assertEquals(80, mine.getQuantiteActuelle());
    }

    @Test
    void testRecolterMineVide() {
        Robot robot = new RobotOr(1, "Alpha", new Position(0, 0), 5, 2);
        Mine mine = new Mine(1, new Position(0, 0), TypeMinerai.OR, 0, 0);

        boolean resultat = robot.recolter(mine);

        assertFalse(resultat);
        assertEquals(0, robot.getStockActuel());
        assertEquals(0, mine.getQuantiteActuelle());
    }

    @Test
    void testRecolterLimiteCapaciteRobot() {
        Robot robot = new RobotOr(1, "Alpha", new Position(0, 0), 5, 3);
        Mine mine = new Mine(1, new Position(0, 0), TypeMinerai.OR, 80, 80);

        robot.recolter(mine);
        robot.recolter(mine);

        assertEquals(5, robot.getStockActuel());
        assertEquals(75, mine.getQuantiteActuelle());
    }

    @Test
    void testRecolterLimiteMinePresqueVide() {
        Robot robot = new RobotOr(1, "Alpha", new Position(0, 0), 5, 3);
        Mine mine = new Mine(1, new Position(0, 0), TypeMinerai.OR, 1, 1);

        boolean resultat = robot.recolter(mine);

        assertTrue(resultat);
        assertEquals(1, robot.getStockActuel());
        assertEquals(0, mine.getQuantiteActuelle());
    }
}
