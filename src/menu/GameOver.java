package menu;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GameOver extends JFrame {

    public GameOver()
    {
        gameOverUI();
    }

    public void gameOverUI()
    {
        JPanel panel = new JPanel() {
            // Surcharge de la méthode paintComponent pour définir le fond
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK); // Fond noir
                g.fillRect(0, 0, getWidth(), getHeight());

                // Load custom font
                try {
                    Font customFont = Font.createFont(Font.TRUETYPE_FONT, GameOver.class.getResourceAsStream("Melted_Monster.ttf")).deriveFont(80f);
                    customFont = customFont.deriveFont(Font.BOLD, 36);
                    g.setFont(customFont);
                } catch (FontFormatException | IOException e) {
                    e.printStackTrace();
                }

                // Set up Graphics2D for drawing with outline
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.RED);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Centrer le texte "GAME OVER" dans la fenêtre
                FontMetrics fontMetrics = g.getFontMetrics();
                int x = (getWidth() - fontMetrics.stringWidth("GAME OVER")) / 2;
                int y = getHeight() / 2;

                // Draw red outline
                g2d.drawString("GAME OVER", x - 1, y - 1);
                g2d.drawString("GAME OVER", x + 1, y - 1);
                g2d.drawString("GAME OVER", x - 1, y + 1);
                g2d.drawString("GAME OVER", x + 1, y + 1);

                // Draw white fill
                g2d.setColor(Color.ORANGE);
                g2d.drawString("GAME OVER", x, y);
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

        // Ajouter le bouton Quitter en bas à droite
        panel.setLayout(new BorderLayout());
        panel.add(quitButton, BorderLayout.SOUTH);

        add(panel);

        setTitle("Game Over");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void customizeButton(JButton button)
    {
        playSound("res/audio/gameover.wav"); 

        button.setFont(new Font("Arial", Font.BOLD, 14)); // Police
        button.setForeground(Color.decode("#FF4500")); // Couleur du texte 
        button.setBackground(Color.decode("#FFFFFF")); // Couleur de fond
        button.setFocusPainted(false); // Supprimer la bordure de mise au point
        button.setBorderPainted(false); // Supprimer la bordure
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Curseur de la main au survol
    }

    public void playSound(String soundFile) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFile).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
    
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent evt) {
                    if (evt.getType() == LineEvent.Type.STOP) {
                        evt.getLine().close();
                    }
                }
            });
        } catch(UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error with playing sound.");
            e.printStackTrace();
        }
    }
}