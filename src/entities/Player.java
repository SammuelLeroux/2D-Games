package entities;

// util
import java.util.List;
import java.util.ArrayList;

// constante
import static utilz.Constants.Directions.*;
import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.Screen.*;

// graphique
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Image;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;

import javax.imageio.ImageIO;

// mes imports
import weapons.*;
import tools.*;

import static main.Game.zombieList;

// menu
import menu.GameOver;

// obstacles
import static main.ProceduralMap.obstaclesList;

// musiques
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent;

public class Player extends Entity
{
    public static int score = 0;
    public static int gold = 0;

    // animation
    private BufferedImage img;
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 20;

    // sprite player
    private Image playerImage;
    private int currentFrame = 0;

    // direction
    private int playerAction = IDLE;
    private boolean moving = true;
    private boolean canMove = true;
    private boolean canAttack = true;
    private boolean left, up, right, down;
    public static int orientation = UP;

    // tools
    private Shield protection;

    // weapon
    private Weapon equipedWeapon;

    public Weapon getEquipedWeapon() { return this.equipedWeapon; }
    public Shield getShield() { return this.protection; }
    public boolean isLeft() { return this.left; }
    public boolean isUp() { return this.up; }
    public boolean isRight() { return this.right; }
    public boolean isDown() { return this.down; }
    public boolean canMove() { return this.canMove; }
    public boolean canAttack() { return this.canAttack; }
    public Image getPlayerImage() { return this.playerImage; }
    public int getOrientation() { return orientation; }
    public int getScore() { return score; }
    public int getGold() { return gold; }

    public void setShield(Shield shield)
    {
        System.out.println("Le bouclier est la pour me proteger.");
        this.protection = shield;
    }
    public void setLeft(boolean left) { this.left = left; }
    public void setUp(boolean up) { this.up = up; }
    public void setRight(boolean right) { this.right = right; }
    public void setDown(boolean down) { this.down = down; }
    public void setCanMove(boolean canMove) { this.canMove = canMove; }
    public void setCanAttack(boolean canAttack) { this.canAttack = canAttack; }
    public void setPlayerImage(Image playerImage) { this.playerImage = playerImage; }

    public Player(float x, float y)
    {
        super(100, 2, x, y);
        System.out.println("Player 1 entered the game.");
        System.out.println("Let the party begin !");

        loadAnimations();
    }

    /* Graphique */
    public void update()
    {
        updatePos();
        updateAnimationTick();
        setAnimation();

        // animation de la balled
        if (this.equipedWeapon != null && this.equipedWeapon.getBullet() != null)
        {
            this.equipedWeapon.getBullet().update();
            if (!this.equipedWeapon.getBullet().isActive()) this.equipedWeapon.setBullet(null);
        }
    }

    public void render(Graphics g)
    {
        // ANIMATION JOUEUR
        g.drawImage(animations[orientation][aniIndex], (int) x, (int) y, 64, 64, null);

        // animation tir
        if (this.equipedWeapon != null)
        {
            if (this.equipedWeapon != null && this.equipedWeapon.getBullet() != null && this.equipedWeapon.getBullet().isActive())
            {
                this.equipedWeapon.draw(g, this.equipedWeapon.getName());
            }
        }
    }

    private void updateAnimationTick() {

        aniTick ++;
       if(aniTick >= aniSpeed) {
           aniTick = 0;
           aniIndex++;
           if (aniIndex >= GetSpriteAmount(playerAction))
           {
                aniIndex = 0;
           }
       }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void loadAnimations()
    {
        try {
            InputStream is = getClass().getResourceAsStream("/joueur1.png");
            this.img = ImageIO.read(is);

            int rows = 4;
            int cols = 3;
            
            this.animations = new BufferedImage[rows][cols];
            for (int j = 0; j < rows; j++)
            {
                for (int i = 0; i < cols; i++)
                {
                    this.animations[j][i] = img.getSubimage(i * spriteWidth, j * spriteHeight, spriteWidth, spriteHeight);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAnimation() {

        int startAni = playerAction;
		if(moving)
        {
			playerAction = RUNNING;
            this.setPlayerImage(this.animations[orientation][this.currentFrame]);
            this.currentFrame = (this.currentFrame + 1) % this.animations[0].length;
        }
		else
        {
            this.setPlayerImage(this.animations[orientation][1]);

			playerAction = IDLE;
        }

        if (startAni != playerAction)
        {
            resetAniTick();
        }
	}
    /* Graphique */

    /* Direction */
    public void resetDirBooleans() {
        left = false;
        up = false;
        right = false;
        down = false;
    }

    private void updatePos() {

        moving = false;

        boolean stopped = false;

        if (left && !right)
        {
            if (x >= super.speed) 
            {
                for (int i = 0; i < obstaclesList.length; i++)
                {
                    if (y <= obstaclesList[i][1] + obstacleSize / 1.5 && y >= obstaclesList[i][1] - obstacleSize / 1.5 && x - super.speed >= obstaclesList[i][0] - spriteWidth && x - super.speed <= obstaclesList[i][0] + spriteWidth)
                    {
                        stopped = true;
                    }
                }

                if (!stopped) x -= super.speed;
            }
            orientation = LEFT;
            moving = true;
        }
        else if (right && !left)
        {
            if (x < WIDTH - spriteWidth * 2)
            {
                for (int i = 0; i < obstaclesList.length; i++)
                {
                    if (y <= obstaclesList[i][1] + obstacleSize / 1.5 && y >= obstaclesList[i][1] - obstacleSize / 1.5 && x + super.speed >= obstaclesList[i][0] - spriteWidth && x + super.speed <= obstaclesList[i][0] + spriteWidth)
                    {
                        stopped = true;
                    }
                }

                if (!stopped) x += super.speed; // aller à droite
            }
            orientation = RIGHT;
            moving = true;
        }

        if (up && !down)
        {
            if (y >= super.speed)
            {
                for (int i = 0; i < obstaclesList.length; i++)
                {
                    if (x <= obstaclesList[i][0] + obstacleSize / 1.5 && x >= obstaclesList[i][0] - obstacleSize / 1.5 && y - super.speed >= obstaclesList[i][1] - obstacleSize && y - super.speed <= obstaclesList[i][1] + obstacleSize)
                    {
                        stopped = true;
                    }
                }

                if (!stopped) y -= super.speed; // aller en haut
            }
            orientation = UP;
            moving = true;
        }
        else if (down && !up)
        {
            if (y < HEIGHT - spriteHeight * 5.5)
            {
                for (int i = 0; i < obstaclesList.length; i++)
                {
                    if (x <= obstaclesList[i][0] + obstacleSize / 1.5 && x >= obstaclesList[i][0] - obstacleSize / 1.5 && y + super.speed >= obstaclesList[i][1] - obstacleSize && y + super.speed <= obstaclesList[i][1] + obstacleSize)
                    {
                        stopped = true;
                    }
                }

                if (!stopped) y += super.speed; // aller en bas
            }
            orientation = DOWN;
            moving = true;
        }
    }
    /* Direction */
    
    public void equip(Weapon weapon)
    {
        if (this.equipedWeapon == null || weapon != this.equipedWeapon)
        {
            // le joueur choisi une arme
            this.equipedWeapon = weapon;
            System.out.println("Le joueur s\'est équipé d'un :" + this.equipedWeapon.getName());
        }
    }
    
    public void attack()
    {
        if (this.equipedWeapon != null && this.canAttack())
        {
            float startX = super.getX();
            float startY = super.getY();
            int axeTir = orientation;

            // player attaque avec l'arme
            boolean canShot = false; // variable pour savoir si on a pu tirer
            if (axeTir == UP)
            {
                canShot = this.equipedWeapon.fire(axeTir, startY/*- 16*/, startY - this.equipedWeapon.getRange(), startX/* + 16*/, this.equipedWeapon.getBulletSpeed());
            }
            else if (orientation == DOWN)
            {
                canShot = this.equipedWeapon.fire(axeTir, startY/* + 32 * 2*/, startY + this.equipedWeapon.getRange(), startX/* + 32*/, this.equipedWeapon.getBulletSpeed());
            }
            else if (axeTir == LEFT)
            {
                canShot = this.equipedWeapon.fire(axeTir, startX/* - 16*/, startX - this.equipedWeapon.getRange(), startY/* + 32*/, this.equipedWeapon.getBulletSpeed());
            }
            else if (axeTir == RIGHT)
            {
                canShot = this.equipedWeapon.fire(axeTir, startX/* + 32*/, startX + this.equipedWeapon.getRange(), startY/* + 32*/, this.equipedWeapon.getBulletSpeed());
            }

            if (canShot)
            {
                // si on a pu tirer, on regarde si la balle a touché un zombie

                // on recupere les enemies s'il y en a sur le chemin de la balle
                Zombie zombie = null;
                List<Zombie> enemiesInRange = new ArrayList<>();
                for (Zombie entity : zombieList)
                {
                    if (entity instanceof Zombie)
                    {
                        switch (axeTir)
                        {
                            case LEFT:
                                // gauche
                                if (entity.getX() <= startX && entity.getX() >= startX - this.equipedWeapon.getRange())
                                {
                                    if (entity.getY() >= startY - spriteHeight && entity.getY() <= startY + spriteHeight) enemiesInRange.add(entity);
                                }
                                break;
                            case RIGHT:
                                // droite
                                if (entity.getX() >= startX && entity.getX() <= startX + this.equipedWeapon.getRange())
                                {
                                    if (entity.getY() >= startY - spriteHeight && entity.getY() <= startY + spriteHeight) enemiesInRange.add(entity);
                                }
                                break;
                            case DOWN:
                                // bas
                                if (entity.getY() >= startY && entity.getY() >= startY + this.equipedWeapon.getRange())
                                {
                                    if (entity.getX() >= startX - spriteWidth && entity.getX() <= startX + spriteWidth) enemiesInRange.add(entity);
                                }
                                break;
                            case UP:
                                // haut
                                if (entity.getY() <= startY && entity.getY() >= startY - this.equipedWeapon.getRange())
                                {
                                    if (entity.getX() >= startX - spriteWidth && entity.getX() <= startX + spriteWidth) enemiesInRange.add(entity);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
    
                for (int i = 0; i < enemiesInRange.size(); i++)
                {
                    // on recupere celui qui est le plus pres, si il y en a plusieur, on recupere le premier de la liste
    
                    float distanceX = enemiesInRange.get(0).getX();
                    float distanceY = enemiesInRange.get(0).getY();
    
                    switch (axeTir)
                    {
                        case 1:
                            if (enemiesInRange.get(i).getX() >= distanceX)
                            {
                                distanceX = enemiesInRange.get(i).getX();
                                zombie = enemiesInRange.get(i);
                            }
                            break;
                        case 2:
                            if (enemiesInRange.get(i).getX() <= distanceX)
                            {
                                distanceX = enemiesInRange.get(i).getX();
                                zombie = enemiesInRange.get(i);
                            }
                            break;
                        case 0:
                            if (enemiesInRange.get(i).getY() <= distanceY)
                            {
                                distanceY = enemiesInRange.get(i).getY();
                                zombie = enemiesInRange.get(i);
                            }
                            break;
                        case 3:
                            if (enemiesInRange.get(i).getY() >= distanceY)
                            {
                                distanceY = enemiesInRange.get(i).getY();
                                zombie = enemiesInRange.get(i);
                            }
                            break;
                        default:
                            break;
                    }
                }
    
                // zombie recoit les degats
                if (zombie != null)
                {
                    if (zombie.receiveDamage(this.equipedWeapon.getDamage()))
                    {
                        
                    }
                    // System.out.println("bullet active ? : " + this.equipedWeapon.getBullet().isActive());
                    // zombie.receiveDamage(this.equipedWeapon.getDamage());

                    // modeliser arret de la balle a l'impact avec un zombie
                    if (this.equipedWeapon.getBullet() != null)
                    {
                        if (
                            (axeTir == UP && this.equipedWeapon.getBullet().getY() < zombie.getY() - spriteHeight) ||
                            (axeTir == DOWN && this.equipedWeapon.getBullet().getY() > zombie.getY() + spriteHeight) ||
                            (axeTir == LEFT && this.equipedWeapon.getBullet().getX() < zombie.getX() - spriteWidth) ||
                            (axeTir == RIGHT && this.equipedWeapon.getBullet().getX() > zombie.getX() + spriteWidth)
                        )
                        {
                            // zombie.receiveDamage(this.equipedWeapon.getDamage());
                            this.equipedWeapon.getBullet().setActivated(false);
                        }
                    }

                    // System.out.println("bullet active ? : " + this.equipedWeapon.getBullet().isActive());

                    zombie = null;
                }
            }
        }
    }

    @Override
    public boolean receiveDamage(int damage)
    {
        if (protection != null)
        {
            // le joueur a une protection, c'est elle qui prend les dégats
            if (!protection.receiveDamage(damage))
            {
                // la protection a été détruite -> on lui retire
                this.protection = null;
            }

            return true;
        }
        else
        {
            // le joueur n'a pas de protection, c'est lui qui prend les dégats
            if (super.receiveDamage(damage))
            {
                System.out.println("Outch, ça fait mal");
                if (super.getHp() <= 0)
                {
                    System.out.println("Je suis mort");
                    new GameOver();
                }

                return true;
            }
            else
            {
                return false;
            }
        }
    }

    /* Tools */
    public boolean nearShop(Gandalf gandalf)
    {
        if (
            (super.getX() <= gandalf.getX() + spriteWidth * 2 && super.getX() >= gandalf.getX() - spriteWidth * 2) &&
            (super.getY() <= gandalf.getY() + spriteHeight * 2 && super.getY() >= gandalf.getY() - spriteHeight * 2)
        )
        {
            // ouverture du shop (menu)
            return true;
        }
        else return false;
    }

    public void buyBonus(Bonus bonus)
    {
        // on perd des golds
        if (gold >= bonus.getCoutBonus())
        {
            gold -= bonus.getCoutBonus();
        
            // on utilise la fonction du bonus
            if (bonus instanceof Shield) this.setShield(((Shield) bonus));
            else
            {
                if (bonus instanceof Potion) ((Potion) bonus).useBonus(this);
                else if (bonus instanceof MunitionMax) ((MunitionMax) bonus).useBonus(this);
            }

            // bonus est utilise -> on ne peut plus le selectionner dans le shop
            bonus.setUsed(true);
            System.out.println(bonus.getName() + " bonus has been bought");
        }
        else
        {
            System.out.println("Not enough gold !");
        }
    }

    public void recoverHp(int value)
    {
        if (super.getHp() + value > 100) super.hp = 100;
        else super.hp = super.getHp() + value;
    }
    /* Tools */

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