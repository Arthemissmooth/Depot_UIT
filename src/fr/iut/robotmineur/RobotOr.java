package fr.iut.robotmineur;

public class RobotOr extends Robot {

    public RobotOr(int id,
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
        return TypeMinerai.OR;
    }
}