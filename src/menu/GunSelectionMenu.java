package menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import java.io.File;
import java.util.List;

import weapons.Weapon;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class GunSelectionMenu extends JFrame
{
    private String selectedGun;
    private GunSelectionCallback callback;
    private Clip selectionSound;

    public String getSelectedGun() {
        return selectedGun;
    }

    public interface GunSelectionCallback
    {
        void onGunSelected(String selectedGun);
    }

    public GunSelectionMenu(List<Weapon> weaponList)
    {
        setTitle("Choix de l'arme");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setResizable(false);
        
        // Handle window iconify (minimize) event
        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if ((e.getNewState() & JFrame.ICONIFIED) == JFrame.ICONIFIED) {
                    // Prevent window from being iconified (minimized)
                    setExtendedState(JFrame.NORMAL);
                }
            }
        });
        getContentPane().setBackground(Color.decode("#1E1E1E"));

        initializeSelectionSound();

        JButton pistolButton = createGunButton("Pistolet", "./res/weapons/pistolet.png");
        JButton machineGunButton = createGunButton("Mitrailleuse", "./res/weapons/mitrailleuse.png");
        JButton sniperButton = createGunButton("Sniper", "./res/weapons/sniper.png");

        pistolButton.addActionListener(new GunSelectionListener("Pistolet"));
        machineGunButton.addActionListener(new GunSelectionListener("Mitrailleuse"));
        sniperButton.addActionListener(new GunSelectionListener("Sniper"));


        add(pistolButton);
        for (Weapon weapon : weaponList)
        {
            if (weapon.getName().equals("Mitrailleuse"))
            {
                if (weapon.getBarrel() > 0)
                {
                    add(machineGunButton);
                }
            }

            if (weapon.getName().equals("Sniper"))
            {
                if (weapon.getBarrel() > 0)
                {
                    add(sniperButton);
                }
            }
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeSelectionSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("res/audio/choixarme.wav"));
            selectionSound = AudioSystem.getClip();
            selectionSound.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JButton createGunButton(String gunName, String imagePath) {
        JButton button = new JButton(gunName);
        ImageIcon icon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        
        button.setIcon(icon);
        button.setBackground(Color.decode("#FFFFFF"));
        button.setForeground(Color.decode("#FF4500"));
        return button;
    }

    private class GunSelectionListener implements ActionListener {
        private String gunName;

        public GunSelectionListener(String gunName) {
            this.gunName = gunName;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            
            selectedGun = gunName;
            System.out.println("Selected Gun: " + gunName);

            // Notify the callback that the gun is selected
            if (callback != null) {
                callback.onGunSelected(selectedGun);
            }

            selectionSound.start();

            // fermer la fenetre
            GunSelectionMenu.this.dispose();
        }
    }

    // Add a method to set the callback
    public void setGunSelectionCallback(GunSelectionCallback callback) {
        this.callback = callback;
    }
}
