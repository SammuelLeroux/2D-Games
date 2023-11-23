package menu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
public class PauseMenu extends JPanel {

    private JButton resumeButton;
    private JButton restartButton;
    private JButton quitButton;

    public PauseMenu(ActionListener resumeListener, ActionListener restartListener, ActionListener quitListener) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Use BoxLayout with Y_AXIS alignment
        setBackground(new Color(0, 0, 0, 0));

        Font zombieFont;
        try {
            zombieFont = Font.createFont(Font.TRUETYPE_FONT, PauseMenu.class.getResourceAsStream("Melted_Monster.ttf")).deriveFont(80f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            zombieFont = new Font("Arial", Font.BOLD, 30); // Use a default font if loading fails
        }

        Font buttonFont = zombieFont.deriveFont(50f);

        JLabel label = new JLabel("Game Paused");
        label.setFont(zombieFont);
                label.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the label
        label.setForeground(Color.RED);
        add(label);
    
        resumeButton = new JButton("Resume");
        resumeButton.setFont(buttonFont);
        resumeButton.addActionListener(resumeListener);
        resumeButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the button
       resumeButton.setBorder(new EmptyBorder(10, 50, 10, 50));
       resumeButton.setForeground(Color.RED);
       add(resumeButton);

        add(Box.createRigidArea(new Dimension(0, 10)));
    
        restartButton = new JButton("Restart");
        restartButton.setFont(buttonFont);
        restartButton.addActionListener(restartListener);
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the button
        restartButton.setBorder(new EmptyBorder(10, 50, 10, 50));
        restartButton.setForeground(Color.RED);
        add(restartButton);

        add(Box.createRigidArea(new Dimension(0, 10)));
    
        quitButton = new JButton("Quit");
        quitButton.setFont(buttonFont);
        quitButton.addActionListener(quitListener);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the button
        quitButton.setBorder(new EmptyBorder(10, 50, 10, 50));
        quitButton.setForeground(Color.RED);
        add(quitButton);

        add(Box.createRigidArea(new Dimension(0, 10)));

    }
}