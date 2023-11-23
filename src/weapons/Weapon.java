package weapons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public abstract class Weapon implements WeaponAction
{
    protected String name;
    protected int damage;
    protected int range;
    protected int nbBullet;
    protected int bulletSpeed;
    protected int barrel;
    protected Bullet bullet;

    private Timer timer;

    public String getName() { return this.name; }
    public int getDamage() { return this.damage; }
    public int getRange() { return this.range; }
    public Bullet getBullet() { return this.bullet; }
    public int getNbBullet() { return this.nbBullet; }
    public int getBulletSpeed() { return this.bulletSpeed; }
    public int getBarrel() { return this.barrel; }

    public void setBullet(Bullet bullet)
    {
        this.bullet = bullet;
    }

    public Weapon(String name, int damage)
    {
        this.name = name;
        this.damage = damage;
    }
    public Weapon(String name, int damage, int range, int nbBullet, int bulletSpeed)
    {
        this.name = name;
        this.damage = damage;
        this.range = range;
        this.nbBullet = nbBullet;
        this.bulletSpeed = bulletSpeed;
    }

    protected abstract Bullet createBullet(int orientation, float start, float end, float axeConstant, int bulletSpeed);
    
    @Override
    public void reloadWeapon(int value)
    {
        if (this.barrel > 0 && this.nbBullet <= 0)
        {
            // on recharge le nombre de balle
            this.nbBullet += value;
            
            // on enleve un chargeur
            this.barrel--;
        }
    }

    @Override
    public boolean fire(int orientation, float start, float end, float axeConstant, int bulletSpeed)
    {
        if (this.nbBullet > 0)
        {
            /*
            // empecher le tir a travers les obstacles
            for (int i = 0; i < obstaclesList.length; i++)
            {
                if ((orientation == LEFT || orientation == RIGHT) && Range(start + 16, obstaclesList[i][0] - obstacleSize / 2, obstaclesList[i][0] + obstacleSize / 2) && Range(axeConstant, obstaclesList[i][1] - obstacleSize, obstaclesList[i][1] + obstacleSize))
                {
                    return false;
                }
                else if ((orientation == UP || orientation == DOWN) && Range(start + 16, obstaclesList[i][1] - obstacleSize / 2, obstaclesList[i][1] + obstacleSize / 2) && Range(axeConstant, obstaclesList[i][0] - obstacleSize, obstaclesList[i][0] + obstacleSize))
                {
                    return false;
                }
            }
            */

            System.out.println("PAN!");
            playSound("res/audio/gun-gunshot-02.wav");
            
            Bullet bullet = createBullet(orientation, start, end, axeConstant, bulletSpeed);
            this.setBullet(bullet);

            timer = new Timer(1000 * this.bullet.getSpeed(), new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (bullet != null && bullet.isActive()) {
                        attack();
                    } else {
                        timer.stop(); // Stop the timer when the bullet is inactive
                    }
                }
            });
    
            timer.start();

            this.nbBullet--;

            return true;
        }
        else
        {
            System.out.println("No more bullets");
            return false;
        }
    }

    public void attack()
    {
        bullet.update();
    }

    public void draw(Graphics g, String weaponName) {
        // Draw the bullet trajectory
        if (bullet != null && bullet.isActive())
        {
            bullet.draw(g, weaponName);
        }
    }

    public void playSound(String soundFile) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFile).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }
}
