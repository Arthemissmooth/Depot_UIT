package assets;

import javax.swing.*;
import java.net.URL;

public class AssetManager {

    public static final ImageIcon ROBOTNICKEL = load("/assets/RobotNickel.png");
    public static final ImageIcon ROBOTOR = load("/assets/RobotOr.png");


    public static final ImageIcon MINEOR = load("/assets/MineOr.png");
    public static final ImageIcon MINENICKEL = load("/assets/MineNickel.png");

    public static final ImageIcon ENTREPOT = load("/assets/warehouse.png");

    public static final ImageIcon EAU = load("/assets/water.png");
    public static final ImageIcon TERRAIN = load("/assets/grass.png");
    public static final ImageIcon BACKGROUND = load("/assets/background.png");

    private static ImageIcon load(String path) {

        URL url = AssetManager.class.getResource(path);

        if (url == null) {
            System.out.println("Image introuvable : " + path);
            return null;
        }

        return new ImageIcon(url);
    }
}