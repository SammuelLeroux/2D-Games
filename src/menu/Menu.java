package menu;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import main.Game;

public class Menu extends JFrame
{
    private static final String FONT_FILE_PATH = "./src/menu/Melted_Monster.ttf";

    public Menu()
    {
        // demarer
        initUI();
    }

    private void initUI()
    {
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(FONT_FILE_PATH));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

            JLabel gameNameLabel = new JLabel("Zombie Holocaust");
            gameNameLabel.setFont(customFont.deriveFont(Font.BOLD, 36));
            gameNameLabel.setForeground(Color.decode("#FFFFFF"));
    
            JButton startButton = new JButton("Start Game");
            JButton exitButton = new JButton("Exit");
    
            // Personnalisation des boutons
            customizeButton(startButton);
            customizeButton(exitButton);
    
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose(); // Close the menu window
                    new Game(); // Start the game
                }
            });
    
            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0); // Exit the application
                }
            });
    
            JPanel panel = new JPanel(new GridBagLayout()) {
                // Surcharge de la méthode paintComponent pour définir le fond
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(Color.decode("#1E1E1E"));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            
            GridBagConstraints gbc = new GridBagConstraints();
    
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 10, 10, 10); // Espacement
    
            panel.add(gameNameLabel, gbc); // Add the game name label
    
            gbc.gridy = 1;
            panel.add(startButton, gbc);
    
            gbc.gridy = 2; // Adjust the grid position for the exit button
            panel.add(exitButton, gbc);
    
            add(panel);
    
            setTitle("Game Menu");
            setSize(500, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setResizable(false);
            setVisible(true);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    private void customizeButton(JButton button)
    {
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Police
        button.setForeground(Color.decode("#FF4500")); // Couleur du texte 
        button.setBackground(Color.decode("#FFFFFF")); // Couleur de fond
        button.setFocusPainted(false); // Supprimer la bordure de mise au point
        button.setBorderPainted(false); // Supprimer la bordure
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Curseur de la main au survol
    }
}
