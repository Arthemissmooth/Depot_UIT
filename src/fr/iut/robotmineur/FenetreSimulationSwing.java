package fr.iut.robotmineur;

import assets.AssetManager;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

public class FenetreSimulationSwing extends JFrame {

    // Palette
    private static final Color BG_DARK      = new Color(10,  14,  26);
    private static final Color BG_PANEL     = new Color(16,  22,  42);
    private static final Color BG_CARD      = new Color(22,  30,  56);
    private static final Color BG_CARD_HOV  = new Color(28,  40,  72);
    private static final Color ACCENT       = new Color(56, 189, 248);   // cyan
    private static final Color ACCENT2      = new Color(250, 204,  21);  // yellow
    private static final Color ACCENT3      = new Color(74, 222, 128);   // green
    private static final Color TEXT_PRIMARY = new Color(226, 232, 240);
    private static final Color TEXT_MUTED   = new Color(100, 116, 139);
    private static final Color BORDER_COL   = new Color(30,  41,  59);

    // Fonts
    private static final Font FONT_TITLE = new Font("Courier New", Font.BOLD,  22);
    private static final Font FONT_H2    = new Font("Courier New", Font.BOLD,  13);
    private static final Font FONT_MONO  = new Font("Courier New", Font.PLAIN, 12);
    private static final Font FONT_LABEL = new Font("Courier New", Font.PLAIN, 11);
    private static final Font FONT_BADGE = new Font("Courier New", Font.BOLD,  11);

    // State
    private final Monde monde;

    private JPanel  panneauGrille;
    private JPanel  zoneInfos;
    private JLabel  labelTour;
    private JLabel  labelStatus;

    private JComboBox<Robot>     comboRobots;
    private JComboBox<String>    comboActions;
    private JComboBox<Direction> comboDirections;

// Statu Robot

    private Robot robotActif;

    public FenetreSimulationSwing(Monde monde) {
        this.monde = monde;
        setTitle("Simulateur de robots mineurs");
        setSize(1380, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 0));
        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
        rafraichir();
    }

    // HEADER


    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(8, 12, 28), getWidth(), 0, new Color(14, 20, 44));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(ACCENT);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(14, 24, 14, 24));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);

        JLabel dot = new JLabel("●") {
            private boolean on = true;
            {
                setFont(new Font("Courier New", Font.BOLD, 14));
                setForeground(ACCENT3);
                new Timer(700, e -> { on = !on; setForeground(on ? ACCENT3 : ACCENT3.darker().darker()); }).start();
            }
        };
        JLabel title    = new JLabel("MINER-BOT");   title.setFont(FONT_TITLE); title.setForeground(TEXT_PRIMARY);
        JLabel subtitle = new JLabel(" @ SIMULATEUR DE ROBOT MINER"); subtitle.setFont(FONT_LABEL); subtitle.setForeground(TEXT_MUTED);
        left.add(dot); left.add(title); left.add(subtitle);

        labelTour = new JLabel();
        labelTour.setFont(new Font("Courier New", Font.BOLD, 14));
        labelTour.setForeground(ACCENT);
        labelTour.setBorder(new CompoundBorder(new LineBorder(ACCENT, 1), new EmptyBorder(6, 16, 6, 16)));

        return header;
    }

    private JPanel buildStatusDot(String label, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0)); p.setOpaque(false);
        JLabel dot = new JLabel("◆"); dot.setFont(new Font("Courier New", Font.BOLD, 9)); dot.setForeground(color);
        JLabel lbl = new JLabel(label); lbl.setFont(FONT_LABEL); lbl.setForeground(TEXT_MUTED);
        p.add(dot); p.add(lbl); return p;
    }


    // CENTER


    private JPanel buildCenter() {
        JPanel centre = new JPanel(new BorderLayout(12, 0));
        centre.setBackground(BG_DARK);
        centre.setBorder(new EmptyBorder(14, 16, 8, 16));

        JPanel gridWrapper = buildCard("CARTE DU TERRAIN");
        panneauGrille = new JPanel(new GridLayout(10, 10, 3, 3));
        panneauGrille.setBackground(BG_CARD);
        panneauGrille.setBorder(new EmptyBorder(10, 10, 10, 10));
        gridWrapper.add(panneauGrille, BorderLayout.CENTER);

        JPanel side = buildSidePanel();
        side.setPreferredSize(new Dimension(300, 0));

        centre.add(gridWrapper, BorderLayout.CENTER);
        centre.add(side,        BorderLayout.EAST);
        return centre;
    }

    private JPanel buildSidePanel() {
        JPanel side = new JPanel(new BorderLayout(0, 10));
        side.setBackground(BG_DARK);

        zoneInfos = new JPanel();
        zoneInfos.setLayout(new BoxLayout(zoneInfos, BoxLayout.Y_AXIS));
        zoneInfos.setBackground(BG_PANEL);
        zoneInfos.setBorder(new EmptyBorder(12, 10, 12, 10));

        JScrollPane scroll = new JScrollPane(zoneInfos);
        scroll.setBorder(new LineBorder(BORDER_COL, 1));
        scroll.getViewport().setBackground(BG_PANEL);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() { thumbColor = new Color(40, 60, 100); trackColor = BG_PANEL; }
        });

        JLabel sideTitle = new JLabel("JOURNAL DE BORD");
        sideTitle.setFont(FONT_H2); sideTitle.setForeground(ACCENT);
        sideTitle.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, BORDER_COL), new EmptyBorder(0, 4, 8, 4)));
        sideTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        side.add(sideTitle, BorderLayout.NORTH);
        side.add(scroll,    BorderLayout.CENTER);
        return side;
    }


    // FOOTER


    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(BG_PANEL); g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(ACCENT); g2.setStroke(new BasicStroke(1.5f));
                g2.drawLine(0, 0, getWidth(), 0); g2.dispose();
            }
        };
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(14, 20, 14, 20));

        JPanel cmdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        cmdPanel.setOpaque(false);
        JLabel cmdLabel = new JLabel("CMD ›"); cmdLabel.setFont(FONT_H2); cmdLabel.setForeground(ACCENT);
        cmdPanel.add(cmdLabel);

        comboRobots = buildStyledCombo();
        for (Robot r : monde.getRobots()) comboRobots.addItem(r);
        cmdPanel.add(buildFieldGroup("ROBOT",     comboRobots));
        cmdPanel.add(buildFieldGroup("ACTION",    comboActions    = buildStyledCombo("AVANCER","RECOLTER","DEPOSER","ATTENDRE")));
        cmdPanel.add(buildFieldGroup("DIRECTION", comboDirections = buildStyledCombo(Direction.values())));

        JButton btnValider = buildActionButton(" EXÉCUTER",     ACCENT,  new Color(0, 60, 100));
        JButton btnTour    = buildActionButton("TOUR SUIVANT", ACCENT2, new Color(80, 60, 0));
        btnValider.addActionListener(e -> executerAction());
        btnTour.addActionListener(e -> { monde.tourSuivant(); rafraichir(); });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(btnValider); btnPanel.add(btnTour);

        labelStatus = new JLabel("Système prêt.");
        labelStatus.setFont(FONT_LABEL); labelStatus.setForeground(ACCENT3);

        footer.add(cmdPanel,    BorderLayout.WEST);
        footer.add(btnPanel,    BorderLayout.EAST);
        footer.add(labelStatus, BorderLayout.SOUTH);
        return footer;
    }

    @SuppressWarnings("unchecked")
    private <T> JComboBox<T> buildStyledCombo(T... items) { JComboBox<T> cb = new JComboBox<>(items); styleCombo(cb); return cb; }
    private <T> JComboBox<T> buildStyledCombo()           { JComboBox<T> cb = new JComboBox<>();       styleCombo(cb); return cb; }

    private void styleCombo(JComboBox<?> cb) {
        cb.setBackground(BG_CARD); cb.setForeground(TEXT_PRIMARY); cb.setFont(FONT_MONO);
        cb.setBorder(new LineBorder(BORDER_COL, 1)); cb.setPreferredSize(new Dimension(140, 30));
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean sel, boolean foc) {
                super.getListCellRendererComponent(l, v, i, sel, foc);
                setBackground(sel ? BG_CARD_HOV : BG_CARD); setForeground(sel ? ACCENT : TEXT_PRIMARY);
                setFont(FONT_MONO); setBorder(new EmptyBorder(4, 8, 4, 8)); return this;
            }
        });
    }

    private JPanel buildFieldGroup(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 3)); p.setOpaque(false);
        JLabel lbl = new JLabel(label); lbl.setFont(FONT_BADGE); lbl.setForeground(TEXT_MUTED);
        p.add(lbl, BorderLayout.NORTH); p.add(field, BorderLayout.CENTER); return p;
    }

    private JButton buildActionButton(String text, Color fg, Color bgDark) {
        JButton btn = new JButton(text) {
            private float hover = 0f;
            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hover = 1f; repaint(); }
                    @Override public void mouseExited(MouseEvent e)  { hover = 0f; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hover > 0 ? new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), 30) : bgDark);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(fg); g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BADGE); btn.setForeground(fg);
        btn.setContentAreaFilled(false); btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        return btn;
    }


    // CARD BUILDER


    private JPanel buildCard(String title) {
        JPanel card = new JPanel(new BorderLayout(0, 6)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(BORDER_COL); g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10); g2.dispose();
            }
        };
        card.setOpaque(false);

        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(BG_DARK); g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(ACCENT); g2.fillRect(0, getHeight() - 2, getWidth(), 2); g2.dispose();
            }
        };
        titleBar.setOpaque(false);
        titleBar.setBorder(new EmptyBorder(10, 14, 10, 14));

        JLabel notch    = new JLabel("▐"); notch.setFont(new Font("Courier New", Font.BOLD, 14)); notch.setForeground(ACCENT);
        JLabel titleLbl = new JLabel(title); titleLbl.setFont(FONT_H2); titleLbl.setForeground(TEXT_PRIMARY);
        titleBar.add(notch); titleBar.add(titleLbl);

        card.add(titleBar, BorderLayout.NORTH);
        return card;
    }


    // GRILLE


    private void afficherGrille() {
        panneauGrille.removeAll();
        for (int l = 0; l < 10; l++)
            for (int c = 0; c < 10; c++)
                panneauGrille.add(creerCase(monde.getSecteur(new Position(l, c)), l, c));
        panneauGrille.revalidate();
        panneauGrille.repaint();
    }

    private JPanel creerCase(Secteur s, int row, int col) {

        CasePanel panel = new CasePanel(s.estEau() ? AssetManager.EAU : AssetManager.TERRAIN);
        boolean actif = (s.getRobot() != null && s.getRobot().equals(robotActif));
        panel.setActif(actif);
        panel.setLayout(new BorderLayout());


        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel coord = new JLabel(" " + row + "," + col);
        coord.setFont(new Font("Courier New", Font.PLAIN, 7));
        coord.setForeground(new Color(80, 100, 140));

        JLabel haut = new JLabel("", SwingConstants.RIGHT);
        haut.setFont(new Font("Courier New", Font.BOLD, 7));

        topBar.add(coord, BorderLayout.WEST);
        topBar.add(haut,  BorderLayout.EAST);

        JLabel center = new JLabel();
        center.setHorizontalAlignment(SwingConstants.CENTER);

        // Icône bas : robot ══
        JLabel bottom = new JLabel();
        bottom.setHorizontalAlignment(SwingConstants.CENTER);

        if (s.getMine() != null) {
            boolean or = s.getMine().getTypeMinerai() == TypeMinerai.OR;
            center.setIcon(resize(or ? AssetManager.MINEOR : AssetManager.MINENICKEL, 32, 32));
            haut.setText((or ? "OR" : "NICKEL") + " ");
            haut.setForeground(or ? ACCENT2 : new Color(160, 200, 240));
        }

        if (s.getEntrepot() != null) {
            center.setIcon(resize(AssetManager.ENTREPOT, 32, 32));
            haut.setText("DÉPÔT ");
            haut.setForeground(ACCENT3);
        }

        if (s.getRobot() != null) {
            boolean or = s.getRobot().getTypeMinerai() == TypeMinerai.OR;
            bottom.setIcon(resize(or ? AssetManager.ROBOTOR : AssetManager.ROBOTNICKEL, 28, 28));


            if (haut.getText().trim().isEmpty()) {
                haut.setText((or ? "ROBOT OR" : "ROBOT NK") + " ");
                haut.setForeground(or ? ACCENT2 : ACCENT);
            }
        }

        panel.add(topBar, BorderLayout.NORTH);   // ← topBar (avec haut dedans) ajouté
        panel.add(center, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        panel.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { panel.setHovered(true);  panel.repaint(); }
            @Override public void mouseExited(MouseEvent e)  { panel.setHovered(false); panel.repaint(); }
        });

        return panel;
    }

    private class CasePanel extends JPanel {
        private final Image img;
        private boolean hovered = false;

        public CasePanel(ImageIcon icon) { this.img = icon.getImage(); setOpaque(false);
        }
        public void setHovered(boolean h) { this.hovered = h; }
        private boolean actif = false;

        public void setActif(boolean actif) {
            this.actif = actif;
        }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            if (hovered) {
                g2.setColor(new Color(56, 189, 248, 40)); g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(ACCENT); g2.setStroke(new BasicStroke(1.5f));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
            if (actif) {
                g2.setColor(new Color(250, 204, 21, 70));
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(new Color(250, 204, 21));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
            g2.dispose();
        }
    }


    // INFOS PANEL


    private void afficherInfos() {
        zoneInfos.removeAll();

        int scoreTotal = monde.getRobots().stream().mapToInt(Robot::getStockActuel).sum();
        zoneInfos.add(buildGlobalScorePanel(scoreTotal));
        zoneInfos.add(Box.createVerticalStrut(10));

        addSectionHeader("ROBOTS ACTIFS", monde.getRobots().size());
        for (Robot r : monde.getRobots()) zoneInfos.add(buildRobotCard(r));

        zoneInfos.add(Box.createVerticalStrut(10));

        // FIX 2 : on itère sur TOUTES les mines (pas de coupure par setMaximumSize)
        addSectionHeader("MINES", monde.getMines().size());
        for (Mine m : monde.getMines()) zoneInfos.add(buildMineCard(m));

        zoneInfos.add(Box.createVerticalStrut(10));

        addSectionHeader("ENTREPÔTS", monde.getEntrepots().size());
        for (Entrepot e : monde.getEntrepots()) zoneInfos.add(buildEntrepotCard(e));

        zoneInfos.add(Box.createVerticalGlue());
        zoneInfos.revalidate();
        zoneInfos.repaint();
    }

    // Score globa

    private JPanel buildGlobalScorePanel(int scoreTotal) {
        JPanel card = new JPanel(new BorderLayout(10, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 50, 20), getWidth(), 0, new Color(20, 40, 15));
                g2.setPaint(gp); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(ACCENT3); g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8); g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(new EmptyBorder(8, 12, 8, 12));

        JLabel icon = new JLabel("★"); icon.setFont(new Font("Courier New", Font.BOLD, 22)); icon.setForeground(ACCENT2);

        JPanel right = new JPanel(new GridLayout(2, 1, 0, 2)); right.setOpaque(false);
        JLabel lbl   = new JLabel("SCORE TOTAL"); lbl.setFont(FONT_LABEL); lbl.setForeground(TEXT_MUTED);
        JLabel score = new JLabel(scoreTotal + " pts"); score.setFont(new Font("Courier New", Font.BOLD, 18)); score.setForeground(ACCENT3);
        right.add(lbl); right.add(score);

        card.add(icon,  BorderLayout.WEST);
        card.add(right, BorderLayout.CENTER);
        return wrapCard(card);
    }

    //  Carte Robot
    private JPanel buildRobotCard(Robot r) {
        boolean   isOr    = r.getTypeMinerai() == TypeMinerai.OR;
        ImageIcon terrain = resize(AssetManager.TERRAIN, 46, 46);
        ImageIcon robIcon = resize(isOr ? AssetManager.ROBOTOR : AssetManager.ROBOTNICKEL, 30, 30);
        Color     accent  = ACCENT;

        int charge   = r.getStockActuel();
        int capacite = r.getCapaciteStockage();

        JPanel card = buildBaseCard(accent);
        // FIX 2 : pas de setMaximumSize → toutes les cartes s'affichent

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(buildTitleRow("Robot #" + r.getId(), isOr ? "OR" : "NICKEL",
                isOr ? ACCENT2 : new Color(180, 200, 220)));
        content.add(Box.createVerticalStrut(3));
        content.add(buildStatRow("Position",  r.getPosition().toString(),  TEXT_MUTED));
        content.add(buildStatRow("Score",     charge + " pts",             ACCENT3));
        content.add(Box.createVerticalStrut(4));
        content.add(buildProgressBar("Charge", charge, capacite, accent));

        card.add(buildTerrainIcon(terrain, robIcon), BorderLayout.WEST);
        card.add(content,                            BorderLayout.CENTER);
        return wrapCard(card);
    }

    // Carte Mine
    private JPanel buildMineCard(Mine m) {
        boolean   isOr    = m.getTypeMinerai() == TypeMinerai.OR;
        ImageIcon terrain = resize(AssetManager.TERRAIN, 46, 46);
        ImageIcon mineIco = resize(isOr ? AssetManager.MINEOR : AssetManager.MINENICKEL, 30, 30);
        Color     accent  = isOr ? ACCENT2 : new Color(160, 190, 220);

        int restant  = m.getMineraiRestant();
        int capacite = m.getQuantiteInitiale();

        JPanel card = buildBaseCard(accent);
        // FIX 2 : pas de setMaximumSize → la 2e mine s'affiche aussi

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(buildTitleRow("Mine #" + m.getId(), isOr ? "OR" : "NICKEL", accent));
        content.add(Box.createVerticalStrut(3));
        content.add(buildStatRow("Restant",  restant + " / " + capacite,      TEXT_MUTED));
        content.add(buildStatRow("Extraits", (capacite - restant) + " minerais", accent));
        content.add(Box.createVerticalStrut(4));
        content.add(buildProgressBar("Réserve", restant, capacite,
                restant == 0 ? new Color(200, 60, 60) : accent));

        card.add(buildTerrainIcon(terrain, mineIco), BorderLayout.WEST);
        card.add(content,                            BorderLayout.CENTER);
        return wrapCard(card);
    }

    //  Carte Entrepôt

    private JPanel buildEntrepotCard(Entrepot e) {
        ImageIcon terrain = resize(AssetManager.TERRAIN,  46, 46);
        ImageIcon entIco  = resize(AssetManager.ENTREPOT, 30, 30);
        Color     accent  = ACCENT3;

        int stock    = e.getStock();
        int capacite = e.getStock();

        JPanel card = buildBaseCard(accent);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(buildTitleRow("Entrepôt #" + e.getId(), "DÉPÔT", accent));
        content.add(Box.createVerticalStrut(3));
        content.add(buildStatRow("Stock",  stock + " / " + capacite,       TEXT_MUTED));
        content.add(buildStatRow("Libre",  (capacite - stock) + " places", accent));
        content.add(Box.createVerticalStrut(4));
        content.add(buildProgressBar("Remplissage", stock, capacite,
                stock >= capacite ? new Color(220, 80, 60) : accent));

        card.add(buildTerrainIcon(terrain, entIco), BorderLayout.WEST);
        card.add(content,                           BorderLayout.CENTER);
        return wrapCard(card);
    }

    //  Composants réutilisables


    private JPanel buildTerrainIcon(ImageIcon terrain, ImageIcon overlay) {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.drawImage(terrain.getImage(), 0, 0, getWidth(), getHeight(), this);
                g2.setColor(new Color(0, 0, 0, 55));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(50, 50));
        JLabel lbl = new JLabel(overlay);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setVerticalAlignment(SwingConstants.CENTER);
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildBaseCard(Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(10, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(accentColor); g2.fillRect(0, 0, 3, getHeight());
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 40));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8); g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(new EmptyBorder(8, 12, 8, 10));
        return card;
    }

    private JPanel buildTitleRow(String title, String tag, Color tagColor) {
        JPanel row = new JPanel(new BorderLayout()); row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
        JLabel t     = new JLabel(title); t.setFont(new Font("Courier New", Font.BOLD, 12)); t.setForeground(TEXT_PRIMARY);
        JLabel badge = new JLabel(" " + tag + " "); badge.setFont(FONT_BADGE); badge.setForeground(tagColor);
        badge.setBorder(new LineBorder(new Color(tagColor.getRed(), tagColor.getGreen(), tagColor.getBlue(), 80), 1));
        row.add(t, BorderLayout.WEST); row.add(badge, BorderLayout.EAST);
        return row;
    }

    private JPanel buildStatRow(String key, String value, Color valueColor) {
        JPanel row = new JPanel(new BorderLayout()); row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15));
        JLabel k = new JLabel(key);   k.setFont(FONT_LABEL); k.setForeground(TEXT_MUTED);
        JLabel v = new JLabel(value); v.setFont(FONT_BADGE); v.setForeground(valueColor);
        row.add(k, BorderLayout.WEST); row.add(v, BorderLayout.EAST);
        return row;
    }

    private JPanel buildProgressBar(String label, int current, int max, Color barColor) {
        JPanel container = new JPanel(new BorderLayout(4, 0));
        container.setOpaque(false);
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));

        JLabel lbl = new JLabel(label); lbl.setFont(new Font("Courier New", Font.PLAIN, 9));
        lbl.setForeground(TEXT_MUTED); lbl.setPreferredSize(new Dimension(62, 14));

        float ratio = (max > 0) ? Math.min(1f, (float) current / max) : 0f;

        JPanel bar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 40, 60)); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                int fillW = (int)(getWidth() * ratio);
                if (fillW > 0) { g2.setColor(barColor); g2.fillRoundRect(0, 0, fillW, getHeight(), 4, 4); }
                g2.dispose();
            }
        };
        bar.setOpaque(false); bar.setPreferredSize(new Dimension(0, 7));

        JLabel pct = new JLabel((int)(ratio * 100) + "%");
        pct.setFont(new Font("Courier New", Font.BOLD, 9)); pct.setForeground(barColor);
        pct.setPreferredSize(new Dimension(30, 14)); pct.setHorizontalAlignment(SwingConstants.RIGHT);

        container.add(lbl, BorderLayout.WEST);
        container.add(bar, BorderLayout.CENTER);
        container.add(pct, BorderLayout.EAST);
        return container;
    }

    private JPanel wrapCard(JPanel card) {
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(card);
        wrapper.add(Box.createVerticalStrut(5));
        return wrapper;
    }

    private void addSectionHeader(String label, int count) {
        JPanel row = new JPanel(new BorderLayout()); row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, BORDER_COL), new EmptyBorder(0, 2, 4, 2)));
        JLabel lbl   = new JLabel(label);         lbl.setFont(FONT_H2); lbl.setForeground(TEXT_MUTED);
        JLabel badge = new JLabel(" " + count + " "); badge.setFont(FONT_BADGE); badge.setForeground(ACCENT);
        badge.setBorder(new LineBorder(new Color(ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(), 80), 1));
        row.add(lbl, BorderLayout.WEST); row.add(badge, BorderLayout.EAST);
        zoneInfos.add(row);
        zoneInfos.add(Box.createVerticalStrut(6));
    }


    // LOGIQUE


    private void executerAction() {
        Robot r = (Robot) comboRobots.getSelectedItem();
        if (r == null) return;
        String action = (String) comboActions.getSelectedItem();


        robotActif = r;

        if ("AVANCER".equals(action)) {
            monde.deplacerRobot(r, (Direction) comboDirections.getSelectedItem());
            setStatus("Robot #" + r.getId() + " déplacé → " + comboDirections.getSelectedItem());
        } else if ("RECOLTER".equals(action)) {
            Secteur s = monde.getSecteur(r.getPosition());
            if (s.getMine() != null) { r.recolter(s.getMine()); setStatus("Robot #" + r.getId() + " a récolté sur Mine #" + s.getMine().getId()); }
            else setStatus("Aucune mine sur la position du robot.");
        } else if ("DEPOSER".equals(action)) {
            Secteur s = monde.getSecteur(r.getPosition());
            if (s.getEntrepot() != null) { r.deposer(s.getEntrepot()); setStatus("Robot #" + r.getId() + " a déposé dans Entrepôt #" + s.getEntrepot().getId()); }
            else setStatus("Aucun entrepôt sur la position du robot.");
        } else {
            setStatus("Robot #" + r.getId() + " attend.");
        }
        rafraichir();
    }

    private void setStatus(String msg) { if (labelStatus != null) labelStatus.setText("› " + msg); }


    private ImageIcon resize(ImageIcon icon, int w, int h) {
        return new ImageIcon(icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
    }

    private void rafraichir() {
        labelTour.setText(" TOUR " + monde.getTourActuel());
        afficherGrille();
        afficherInfos();
    }
}