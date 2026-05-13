package fr.iut.robotmineur;

public class RobotNickel extends Robot {

    public RobotNickel(int id,
                       Position position,
                       int capaciteStockage,
                       int capaciteExtraction) {

        super(id,
                position,
                capaciteStockage,
                capaciteExtraction);
    }

    @Override
    public TypeMinerai getTypeMinerai() {
        return TypeMinerai.NICKEL;
    }
}