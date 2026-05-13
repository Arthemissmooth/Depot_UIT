package fr.iut.robotmineur;

public class RobotOr extends Robot {

    public RobotOr(int id,
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
        return TypeMinerai.OR;
    }
}
