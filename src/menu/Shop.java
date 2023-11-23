package menu;

import java.util.List;

import javax.swing.*;

import main.GamePanel;
import tools.Bonus;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Shop extends JFrame
{
    private GamePanel gamePanel;
    private List<Bonus> bonusList;

    private String selectedBonus;
    private BonusSelectionCallback callback;
    private JTextArea infoTextArea;
    private JButton validateButton;

    private Clip purchaseSound;

    public String getSelectedBonus() {
        return selectedBonus;
    }

    public interface BonusSelectionCallback {
        void onBonusSelected(String selectedBonus);
    }

    public Shop(GamePanel gamePanel, List<Bonus> bonusList)
    {
        this.gamePanel = gamePanel;
        this.bonusList = bonusList;

        setTitle("Choix d'un bonus");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initializePurchaseSound();

        int potionCost = 100;
        int munitionMaxCost = 100;
        int shieldCost = 100;

        for (Bonus bonus : bonusList)
        {
            if (bonus.getName().equals("Potion"))
            {
                potionCost = bonus.getCoutBonus();
            }

            if (bonus.getName().equals("MunitionMax"))
            {
                munitionMaxCost = bonus.getCoutBonus();
            }

            if (bonus.getName().equals("Shield"))
            {
                shieldCost = bonus.getCoutBonus();
            }
        }

        // Create buttons with images and text below
        JButton potionButton = createBonusButton("Potion", "./res/bonus/potion.jpg", "Boire cette potion vous rendra un pourcentage de vos points de vie manquant", potionCost);
        JButton munitionMaxButton = createBonusButton("MunitionMax", "./res/bonus/munition-max.jpeg", "Vous récupérez toutes les munitions sur toutes les armes", munitionMaxCost);
        JButton shieldButton = createBonusButton("Shield", "./res/bonus/shield.jpg", "Equipez vous de ce bouclier pour pouvoir encaisser plus de degats", shieldCost);

        // Create a panel for the bonus buttons using GridLayout
        JPanel bonusPanel = new JPanel(new GridLayout(1, 3)); // 1 row, 3 columns
        
        for (Bonus bonus : bonusList)
        {
            if (bonus.getName().equals("Potion") && !bonus.isUsed())
            {
                bonusPanel.add(potionButton);
            }

            if (bonus.getName().equals("MunitionMax") && !bonus.isUsed())
            {
                bonusPanel.add(munitionMaxButton);
            }

            if (bonus.getName().equals("Shield") && !bonus.isUsed())
            {
                bonusPanel.add(shieldButton);
            }
        }

        /* Passer au level suivant */

        JPanel continuerPanel = new JPanel(new FlowLayout());
        JButton continuerButton = new JButton("Continuer");
        continuerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // fermer la fenetre du shop et update le level
                dispose();
                gamePanel.getGame().closeShop();
            }
        });
        continuerPanel.add(continuerButton);

        /* Passer au level suivant */
        
        // Add panels to the frame
        add(bonusPanel, BorderLayout.NORTH);
        add(createBuyBonus(), BorderLayout.CENTER);
        add(continuerPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializePurchaseSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("res/audio/buy.wav"));
            purchaseSound = AudioSystem.getClip();
            purchaseSound.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createBuyBonus()
    {
       // Create a panel for the information and validation
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setPreferredSize(new Dimension(200, 100));

        // Create an information area without scroll
        infoTextArea = new JTextArea();
        infoTextArea.setEditable(false);
        infoTextArea.setLineWrap(true);
        infoTextArea.setWrapStyleWord(true);
        infoPanel.add(new JScrollPane(infoTextArea), BorderLayout.CENTER);

        // Create a button for validation bonus
        validateButton = new JButton("Valider");
        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Check if a bonus is selected
                if (selectedBonus != null) {
                    System.out.println("Selected Bonus: " + selectedBonus);
                    
                    for (Bonus bonus : bonusList)
                    {
                        if (bonus.getName().equals(selectedBonus))
                        {
                            // le player paie puis utilise le bonus
                            gamePanel.getGame().getPlayer().buyBonus(bonus);
                        }
                    }
        
                    // fermer la fenetre du shop et update le level
                    dispose();
                    gamePanel.getGame().closeShop();
        
                    // Notify the callback that the bonus is selected
                    if (callback != null) {
                        callback.onBonusSelected(selectedBonus);
                    }

                    // Play the purchase sound
                    purchaseSound.start();

                } else {
                    // If no bonus is selected, display a message or handle it accordingly
                    System.out.println("Please select a bonus before clicking Valider.");
                }
            }
        });
        validateButton.setVisible(false); // Initially hide the "Valider" button
        infoPanel.add(validateButton, BorderLayout.SOUTH);

        // Add components to the information panel
        infoPanel.add(infoTextArea, BorderLayout.CENTER);
        infoPanel.add(validateButton, BorderLayout.SOUTH);

        return infoPanel;
    }

    private JButton createBonusButton(String bonusName, String imagePath, String info, int cost) {
        JButton button = new JButton("<html><center>" + bonusName + "</center></html>");
        ImageIcon icon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        button.setIcon(icon);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setBackground(Color.decode("#FFFFFF"));
        button.setForeground(Color.decode("#FF4500"));

        JLabel costLabel = new JLabel("Cost: " + cost);
        costLabel.setForeground(Color.decode("#FF4500"));

        // Use a layout manager to position the components
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(button, BorderLayout.CENTER);
        panel.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.SOUTH); // Add a separator
        panel.add(costLabel, BorderLayout.SOUTH);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                selectedBonus = bonusName;
                System.out.println("Selected Bonus: " + bonusName);

                // Update the information area
                infoTextArea.setText(info);

                // Show the "Valider" button
                validateButton.setVisible(true);

                // Play the purchase sound
                purchaseSound.start();
            }
        });

        // Encapsulate the JPanel in a JButton
        JButton encapsulatedButton = new JButton();
        encapsulatedButton.setLayout(new BorderLayout());
        encapsulatedButton.add(panel, BorderLayout.CENTER);

        return encapsulatedButton;
    }
}
