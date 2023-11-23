package menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GameCompleted extends JFrame {

    public GameCompleted() {
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            // Surcharge de la méthode paintComponent pour définir le fond
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.setColor(Color.decode("#1E1E1E")); // Fond noir
                g.fillRect(0, 0, getWidth(), getHeight());

                /*g.setColor(Color.GREEN); // Couleur du texte
                Font font = new Font("Arial", Font.BOLD, 24);
                g.setFont(font);*/

                // Load custom font
                try {
                    Font customFont = Font.createFont(Font.TRUETYPE_FONT, GameOver.class.getResourceAsStream("Melted_Monster.ttf")).deriveFont(80f);
                    customFont = customFont.deriveFont(Font.BOLD, 36);
                    g.setFont(customFont);
                } catch (FontFormatException | IOException e) {
                    e.printStackTrace();
                }

                g.setColor(Color.GREEN);

                // Centrer le texte "Game successfully completed" dans la fenêtre
                FontMetrics fontMetrics = g.getFontMetrics();
                int x = (getWidth() - fontMetrics.stringWidth("Game successfully completed")) / 2;
                int y = getHeight() / 2 - 30; // Décalage vers le haut
                g.drawString("Game successfully completed", x, y);
            }
        };

        JButton quitButton = new JButton("Quitter");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Quitter l'application
            }
        });

        // Personnalisation du bouton Quitter
        customizeButton(quitButton);

        // Ajouter le texte et le bouton Quitter
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(200, 0, 100, 0); // Espacement
        panel.add(quitButton, gbc);

        gbc.gridy = 1;
        panel.add(quitButton, gbc);

        add(panel);

        setTitle("Game Completed");
        setSize(650, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void customizeButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(45, 45, 45)); // Gris foncé
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Ajout d'une bordure arrondie et d'une légère ombre
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 30, 30), 2),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));
    }
}
