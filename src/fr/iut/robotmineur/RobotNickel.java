package fr.iut.robotmineur;

public class RobotNickel extends Robot {

    public RobotNickel(int id,
                       String nom,
                       Position position,
                       int capaciteStockage,
                       int capaciteExtraction) {

        super(id,
                nom,
                position,
                capaciteStockage,
                capaciteExtraction);
    }

    @Override
    public TypeMinerai getTypeMinerai() {
        return TypeMinerai.NICKEL;
    }
}