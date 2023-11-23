package entities;

import static main.Game.zombieList;
import static utilz.Constants.Screen.*;
import static utilz.Constants.Range;
import static entities.Player.score;
import static entities.Player.gold;

// obstacles
import static main.ProceduralMap.obstaclesList;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public abstract class Zombie extends Entity implements Runnable
{
    protected String name;

    // point
    protected int force;
    protected int point;

    protected static int miniZombieKilled;
    protected static int gouleKilled;
    protected static int megaZombieKilled;
    protected static int bossZombieKilled;

    protected Image zombieImage;
    protected Player target;
    private Direction orientation;
    private int currentFrame = 0;

    private BufferedImage img;
	private BufferedImage[][] animations;

    public String getName() { return this.name; }
    public int getForce() { return this.force; }
    public Player getTarget() { return this.target; }
    public Image getZombieImage() { return this.zombieImage; }
    public int getPoint() { return this.point; }

    public Direction getDirection() { return this.orientation; }

    public enum Direction { LEFT, UP, RIGHT, DOWN };

    public void setTarget(Player player) { this.target = player; }
    public void setZombieImage(Image zombieImage) { this.zombieImage = zombieImage; }
    public void setOrientation(Direction orientation) { this.orientation = orientation; }

    public Zombie(String name, int hp, int force, float speed, int point)
    {
        super(hp, speed, 0, 0); // generer x et y aleatoirement sur le bord de la map
        this.name = name; // type de zombie
        this.force = force; // degat d'attaque du zombie
        this.point = point; //
        
        Point coord = this.generateXY();
        for (int i = 0; i < obstaclesList.length; i++)
        {
            while(Range(coord.x, obstaclesList[i][0] - obstacleSize, obstaclesList[i][0] + obstacleSize) && Range(coord.y, obstaclesList[i][1] - obstacleSize, obstaclesList[i][1] + obstacleSize))
            {
                coord = this.generateXY();
            }
        }
        super.x = coord.x;
        super.y = coord.y;

        System.out.println(this.getName() + " has appeared : (" + super.getX() + ";" + super.getY() + ").");

        loadAnimations(this);
    }

    public Point generateXY()
    {
        int x = (int) Math.round(Math.random());
        int y = 0;
        if (x == 0)
        {
            y = (int) Math.round(Math.random() * (HEIGHT - spriteHeight * 5.5));
        }
        else
        {
            y = 0;
            x = (int) Math.round(Math.random() * (WIDTH - spriteWidth));
        }

        return new Point(x, y);
    }
    public void moveTowards()
    {
        if (this.target.getHp() > 0)
        {
            double xDifference = this.target.getX() - super.getX();
            double yDifference = this.target.getY() - super.getY();
    
            if (!this.tryAttack())
            {
                boolean stopped = false;

                if (xDifference > spriteWidth && super.x <= WIDTH - Math.min(xDifference, this.speed))
                {
                    setOrientation(Direction.RIGHT);
                    for (int i = 0; i < obstaclesList.length; i++)
                    {
                        //if (y <= obstaclesList[i][1] + obstacleSize / 1.2 && y >= obstaclesList[i][1] - obstacleSize / 1.2 && x + super.speed >= obstaclesList[i][0] - obstacleSize && x + super.speed <= obstaclesList[i][0] + obstacleSize)
                        if (Range(y, obstaclesList[i][1] - obstacleSize, obstaclesList[i][1] + obstacleSize) && Range(x + speed, obstaclesList[i][0] - obstacleSize, obstaclesList[i][0] + obstacleSize))
                        {
                            stopped = true;
                        }
                    }
                    if (!stopped) super.x += Math.min(xDifference, this.speed);
                }
                else if (xDifference < spriteWidth && super.x >= Math.min(-xDifference, this.speed))
                {
                    setOrientation(Direction.LEFT);
                    for (int i = 0; i < obstaclesList.length; i++)
                    {
                        // if (y <= obstaclesList[i][1] + obstacleSize / 1.2 && y >= obstaclesList[i][1] - obstacleSize / 1.2 && x - super.speed >= obstaclesList[i][0] - obstacleSize && x - super.speed <= obstaclesList[i][0] + obstacleSize)
                        if (Range(y, obstaclesList[i][1] - obstacleSize, obstaclesList[i][1] + obstacleSize) && Range(x - speed, obstaclesList[i][0] - obstacleSize, obstaclesList[i][0] + obstacleSize))
                        {
                            stopped = true;
                        }
                    }
                    if (!stopped) super.x -= Math.min(-xDifference, this.speed);
                }
                
                if (yDifference > spriteHeight && super.y <= HEIGHT - Math.min(yDifference, this.speed))
                {
                    setOrientation(Direction.UP);
                    for (int i = 0; i < obstaclesList.length; i++)
                    {
                        // if (x <= obstaclesList[i][0] + obstacleSize / 1.2 && x >= obstaclesList[i][0] - obstacleSize / 1.2 && y - super.speed >= obstaclesList[i][1] - obstacleSize && y - super.speed <= obstaclesList[i][1] + obstacleSize)
                        if (Range(x, obstaclesList[i][0] - obstacleSize, obstaclesList[i][0] + obstacleSize) && Range(y - speed, obstaclesList[i][1] - obstacleSize, obstaclesList[i][1] + obstacleSize))
                        {
                            stopped = true;
                        }
                    }
                    if (!stopped) super.y += Math.min(yDifference, this.speed);
                }
                else if (yDifference < spriteHeight && super.y >= Math.min(-yDifference, this.speed))
                {
                    setOrientation(Direction.DOWN);
                    for (int i = 0; i < obstaclesList.length; i++)
                    {
                        // if (x <= obstaclesList[i][0] + obstacleSize / 1.5 && x >= obstaclesList[i][0] - obstacleSize / 1.5 && y + super.speed >= obstaclesList[i][1] - obstacleSize && y + super.speed <= obstaclesList[i][1] + obstacleSize)
                        if (Range(x, obstaclesList[i][0] - obstacleSize, obstaclesList[i][0] + obstacleSize) && Range(y + speed, obstaclesList[i][1] - obstacleSize, obstaclesList[i][1] + obstacleSize))
                        {
                            stopped = true;
                        }
                    }
                    if (!stopped) super.y -= Math.min(-yDifference, this.speed);
                }

                // Mettre à jour l'image pour l'animation
                this.setZombieImage(this.animations[orientation.ordinal()][this.currentFrame]);
                this.currentFrame = (this.currentFrame + 1) % this.animations[0].length;
            }
    
            if (tryAttack()) this.attack();
        }
    }
    public boolean tryAttack()
    {
        if (this.target.getHp() > 0)
        {
            double xDifference = Math.abs(this.target.getX() - super.getX());
            double yDifference = Math.abs(this.target.getY() - super.getY());
    
            if (xDifference < spriteWidth && yDifference < spriteWidth)
            {
                return true;
            }

            return false;
        }

        return false;
    }
    public void attack()
    {
        if (this.target.getHp() > 0 && this.tryAttack())
        {
            System.out.println(this.getName() + ": Je vais te devorer.");

            if (Math.abs(super.x - this.target.getX()) <= spriteWidth * 2 && Math.abs(super.y - this.target.getY()) <= spriteHeight * 2)
            {
                System.out.println("Boss force : " + this.getForce());
                this.target.receiveDamage(this.force);
            }
        }
    }

    @Override
    public boolean receiveDamage(int damage)
    {
        if (super.receiveDamage(damage))
        {
            System.out.println("Prend ça zombie!");
            
            if (super.hp == 0)
            {
                // on incremente le type de zombie tue + le score
                if (this instanceof MiniZombie) {
                    miniZombieKilled++;
                }
                else if (this instanceof Goule) {
                    gouleKilled++;
                }
                else if (this instanceof MegaZombie) {
                    megaZombieKilled++;
                }
                else if (this instanceof BossZombie) {
                    bossZombieKilled++;
                }
    
                // score player
                score += this.getPoint();
                // gold player
                gold += 100; // this.getPoint() * 2;
    
                // Jouer un son
                playSound("res/audio/die.wav");
    
                zombieList.remove(this);
            }
    
            return true;
        }
        else return false;
    }

    private void loadAnimations(Zombie zombie)
    {
        try
        {
            String type = "";
            if (this instanceof MiniZombie)
            {
                type = "miniZombie";
            }
            else if (this instanceof Goule)
            {
                type = "goule";
            }
            else if (this instanceof MegaZombie)
            {
                type = "megaZombie";
            }
            else if (this instanceof BossZombie)
            {
                type = "bossZombie";
            }

            InputStream inputStream = getClass().getResourceAsStream("/" + type + ".png");
            this.img = ImageIO.read(inputStream);
    
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
        } catch (IOException e) {
            e.printStackTrace();
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

    @Override
    public void run()
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }
}