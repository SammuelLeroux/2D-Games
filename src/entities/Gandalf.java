package entities;

// graphique
import java.awt.image.BufferedImage;
import java.awt.Image;

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import static utilz.Constants.Screen.*;
import static utilz.Constants.Directions.*;

public class Gandalf extends Entity
{
    // animation
    private BufferedImage img;
    private Image gandalfImage;
    private Player player;
    private int orientation;

    public Image getGandalfImage() { return this.gandalfImage; }

    public int getOrientation() { return this.orientation; }

    public void setGandalfImage(Image gandalfImage) { this.gandalfImage = gandalfImage; }
    
    public Gandalf(Player player)
    {
        super(WIDTH / 2, HEIGHT / 2);
        this.player = player;
        loadAnimations();
    }

    public void loadAnimations()
    {
        try {
            InputStream is = getClass().getResourceAsStream("/gandalf.png");
            this.img = ImageIO.read(is);

            switch (this.player.getOrientation())
            {
                // Adjust the subimage coordinates based on your sprite sheet layout
                case UP:
                    this.img = img.getSubimage(0, 0, spriteWidth, spriteHeight);
                    break;
                case DOWN:
                    this.img = img.getSubimage(0, spriteHeight, spriteWidth, spriteHeight);
                    break;
                case LEFT:
                    this.img = img.getSubimage(0, 2 * spriteHeight, spriteWidth, spriteHeight);
                    break;
                case RIGHT:
                    this.img = img.getSubimage(0, 3 * spriteHeight, spriteWidth, spriteHeight);
                    break;
            }

            this.gandalfImage = this.img;

            // this.gandalfImage = img.getSubimage(DOWN * spriteHeight, 0, spriteWidth, spriteHeight);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}