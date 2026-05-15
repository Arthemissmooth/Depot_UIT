package fr.iut.robotmineur;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class FenetreSimulationSwing extends JFrame {

    private final Monde monde;

    private JPanel panneauGrille;
    private JTextArea zoneInfos;

    private JComboBox<Robot> comboRobots;
    private JComboBox<String> comboActions;
    private JComboBox<Direction> comboDirections;

    private JLabel labelTour;

    public FenetreSimulationSwing(Monde monde) {
        this.monde = monde;

        setTitle("SAÉ 2.01 - Simulation de robots mineurs");
        setSize(1200, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        add(creerHeader(), BorderLayout.NORTH);
        add(creerCentre(), BorderLayout.CENTER);
        add(creerBas(), BorderLayout.SOUTH);

        rafraichir();
    }

    private JPanel creerHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(20, 35, 60));
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titre = new JLabel("Simulation de robots mineurs");
        titre.setForeground(Color.WHITE);
        titre.setFont(new Font("Arial", Font.BOLD, 26));

        labelTour = new JLabel();
        labelTour.setOpaque(true);
        labelTour.setBackground(new Color(75, 105, 220));
        labelTour.setForeground(Color.WHITE);
        labelTour.setFont(new Font("Arial", Font.BOLD, 20));
        labelTour.setBorder(new EmptyBorder(8, 20, 8, 20));

        header.add(titre, BorderLayout.WEST);
        header.add(labelTour, BorderLayout.EAST);

        return header;
    }

    private JPanel creerCentre() {
        JPanel centre = new JPanel(new BorderLayout(15, 15));
        centre.setBackground(new Color(245, 247, 250));
        centre.setBorder(new EmptyBorder(20, 20, 20, 20));

        panneauGrille = new JPanel(new GridLayout(11, 11, 4, 4));
        panneauGrille.setBackground(new Color(245, 247, 250));

        zoneInfos = new JTextArea();
        zoneInfos.setEditable(false);
        zoneInfos.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollInfos = new JScrollPane(zoneInfos);
        scrollInfos.setPreferredSize(new Dimension(330, 600));
        scrollInfos.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        centre.add(panneauGrille, BorderLayout.CENTER);
        centre.add(scrollInfos, BorderLayout.EAST);

        return centre;
    }

    private JPanel creerBas() {
        JPanel bas = new JPanel(new BorderLayout(10, 10));
        bas.setBackground(new Color(245, 247, 250));
        bas.setBorder(new EmptyBorder(10, 20, 20, 20));

        bas.add(creerLegende(), BorderLayout.NORTH);
        bas.add(creerActions(), BorderLayout.CENTER);

        return bas;
    }

    private JPanel creerLegende() {
        JPanel legende = new JPanel(new GridLayout(1, 5, 10, 0));
        legende.setBackground(Color.WHITE);
        legende.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220)),
                new EmptyBorder(12, 12, 12, 12)
        ));

        legende.add(new JLabel("Mine : M"));
        legende.add(new JLabel("Entrepôt : E"));
        legende.add(new JLabel("Robot : R"));
        legende.add(new JLabel("Eau : X X"));
        legende.add(new JLabel("Terrain : vert"));

        return legende;
    }

    private JPanel creerActions() {
        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
        actions.setBackground(Color.WHITE);
        actions.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220)),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel titre = new JLabel("Actions du tour");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        actions.add(titre);

        actions.add(Box.createVerticalStrut(10));

        JPanel ligne1 = new JPanel(new FlowLayout());
        ligne1.setBackground(Color.WHITE);

        comboRobots = new JComboBox<>();
        for (Robot robot : monde.getRobots()) {
            comboRobots.addItem(robot);
        }

        ligne1.add(new JLabel("Robot :"));
        ligne1.add(comboRobots);

        actions.add(ligne1);

        JPanel ligne2 = new JPanel(new FlowLayout());
        ligne2.setBackground(Color.WHITE);

        comboActions = new JComboBox<>(new String[]{
                "AVANCER",
                "RECOLTER",
                "DEPOSER",
                "ATTENDRE"
        });

        comboDirections = new JComboBox<>(Direction.values());

        ligne2.add(new JLabel("Action :"));
        ligne2.add(comboActions);

        ligne2.add(new JLabel("Direction :"));
        ligne2.add(comboDirections);

        actions.add(ligne2);

        JPanel ligne3 = new JPanel(new FlowLayout());
        ligne3.setBackground(Color.WHITE);

        JButton btnValider = new JButton("Valider l'action");
        JButton btnTourSuivant = new JButton("Tour suivant");

        btnValider.addActionListener(e -> executerAction());
        btnTourSuivant.addActionListener(e -> {
            monde.tourSuivant();
            rafraichir();
        });

        ligne3.add(btnValider);
        ligne3.add(btnTourSuivant);

        actions.add(ligne3);

        return actions;
    }

    private void executerAction() {
        Robot robot = (Robot) comboRobots.getSelectedItem();

        if (robot == null) {
            return;
        }

        String action = (String) comboActions.getSelectedItem();

        if ("AVANCER".equals(action)) {
            Direction direction = (Direction) comboDirections.getSelectedItem();
            boolean ok = monde.deplacerRobot(robot, direction);

            if (!ok) {
                JOptionPane.showMessageDialog(this, "Déplacement impossible.");
            }
        }

        else if ("RECOLTER".equals(action)) {
            Secteur secteur = monde.getSecteur(robot.getPosition());

            if (secteur.getMine() == null) {
                JOptionPane.showMessageDialog(this, "Il n'y a pas de mine ici.");
            } else {
                boolean ok = robot.recolter(secteur.getMine());

                if (!ok) {
                    JOptionPane.showMessageDialog(this, "Récolte impossible.");
                }
            }
        }

        else if ("DEPOSER".equals(action)) {
            Secteur secteur = monde.getSecteur(robot.getPosition());

            if (secteur.getEntrepot() == null) {
                JOptionPane.showMessageDialog(this, "Il n'y a pas d'entrepôt ici.");
            } else {
                boolean ok = robot.deposer(secteur.getEntrepot());

                if (!ok) {
                    JOptionPane.showMessageDialog(this, "Dépôt impossible.");
                }
            }
        }

        else if ("ATTENDRE".equals(action)) {
            JOptionPane.showMessageDialog(this, "Le robot attend.");
        }

        rafraichir();
    }

    private void rafraichir() {
        labelTour.setText("Tour " + monde.getTourActuel());
        afficherGrille();
        afficherInfos();
    }

    private void afficherGrille() {
        panneauGrille.removeAll();

        panneauGrille.add(new JLabel(""));

        for (int colonne = 0; colonne < 10; colonne++) {
            JLabel label = new JLabel(String.valueOf(colonne), SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 16));
            panneauGrille.add(label);
        }

        for (int ligne = 0; ligne < 10; ligne++) {

            JLabel labelLigne = new JLabel(String.valueOf(ligne), SwingConstants.CENTER);
            labelLigne.setFont(new Font("Arial", Font.BOLD, 16));
            panneauGrille.add(labelLigne);

            for (int colonne = 0; colonne < 10; colonne++) {
                Secteur secteur = monde.getSecteur(new Position(ligne, colonne));
                panneauGrille.add(creerCase(secteur));
            }
        }

        panneauGrille.revalidate();
        panneauGrille.repaint();
    }

    private JPanel creerCase(Secteur secteur) {
        JPanel casePanel = new JPanel(new GridLayout(2, 1));
        casePanel.setPreferredSize(new Dimension(70, 60));
        casePanel.setBorder(new LineBorder(new Color(120, 150, 70)));
        casePanel.setBackground(new Color(155, 190, 80));

        JLabel haut = new JLabel("", SwingConstants.CENTER);
        JLabel bas = new JLabel("", SwingConstants.CENTER);

        haut.setFont(new Font("Arial", Font.BOLD, 14));
        bas.setFont(new Font("Arial", Font.BOLD, 14));

        if (secteur.estEau()) {
            casePanel.setBackground(new Color(190, 225, 250));
            haut.setText("X X");
            bas.setText("X X");
        } else {
            if (secteur.getMine() != null) {
                casePanel.setBackground(new Color(200, 240, 175));
                haut.setText("M " + secteur.getMine().getId());
            }

            if (secteur.getEntrepot() != null) {
                casePanel.setBackground(new Color(255, 235, 160));
                haut.setText("E " + secteur.getEntrepot().getId());
            }

            if (secteur.getRobot() != null) {
                bas.setText("R " + secteur.getRobot().getId());
            }
        }

        casePanel.add(haut);
        casePanel.add(bas);

        return casePanel;
    }

    private void afficherInfos() {
        StringBuilder texte = new StringBuilder();

        texte.append("tour ").append(monde.getTourActuel()).append("\n\n");

        texte.append("Mines :\n");
        for (Mine mine : monde.getMines()) {
            texte.append("M").append(mine.getId()).append(" ")
                    .append(mine.getPosition().getLigne()).append(" ")
                    .append(mine.getPosition().getColonne()).append(" ")
                    .append(mine.getTypeMinerai()).append(" ")
                    .append(mine.getQuantiteActuelle()).append(" / ")
                    .append(mine.getQuantiteInitiale()).append("\n");
        }

        texte.append("\nEntrepôts :\n");
        for (Entrepot entrepot : monde.getEntrepots()) {
            texte.append("E").append(entrepot.getId()).append(" ")
                    .append(entrepot.getPosition().getLigne()).append(" ")
                    .append(entrepot.getPosition().getColonne()).append(" ")
                    .append(entrepot.getTypeMinerai()).append(" ")
                    .append(entrepot.getStock()).append("\n");
        }

        texte.append("\nRobots :\n");
        for (Robot robot : monde.getRobots()) {
            texte.append("R").append(robot.getId()).append(" ")
                    .append(robot.getPosition().getLigne()).append(" ")
                    .append(robot.getPosition().getColonne()).append(" ")
                    .append(robot.getTypeMinerai()).append(" ")
                    .append(robot.getStockActuel()).append(" / ")
                    .append(robot.getCapaciteStockage()).append("\n");
        }

        zoneInfos.setText(texte.toString());
    }
}
