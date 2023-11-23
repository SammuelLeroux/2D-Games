package weapons;

import static utilz.Constants.Directions.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bullet
{
    private float start;
    private float end;
    public int orientation;

    private float x;
    private float y;

    private int speed;
    private boolean activated;

    private BufferedImage bulletImage;

    public Bullet(int orientation, float start, float end, float axeConstant, int bulletSpeed)
    {
        this.start = start;
        this.end = end;
        this.orientation = orientation;
        if (orientation == LEFT || orientation == RIGHT)
        {
            this.x = start;
            this.y = axeConstant;
        }
        else if (orientation == UP || orientation == DOWN)
        {
            this.y = start;
            this.x = axeConstant;
        }

        this.speed = bulletSpeed;

        this.activated = true;
    }

    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public int getSpeed() { return this.speed; }
    public boolean isActive() { return this.activated; }

    public void setActivated(boolean activated) { this.activated = activated; }

    public void update()
    {
        if ((orientation == LEFT || orientation == RIGHT) && this.x == end)
        {
            this.activated = false;
        }
        else if ((orientation == UP || orientation == DOWN) && this.y == end)
        {
            this.activated = false;
        }

        if (this.activated)
        {
            switch (this.orientation)
            {
                case UP:
                    if (this.y <= start && this.y > end) this.y -= this.speed;
                    break;
                case DOWN:
                    if (this.y >= start && this.y < end) this.y += this.speed;
                    break;
                case RIGHT:
                    if (this.x >= start && this.x < end) this.x += this.speed;
                    break;
                case LEFT:
                    if (this.x <= start && this.x > end) this.x -= this.speed;
                    break;
                default:
                    break;
            }
        }
    }

    public void draw(Graphics g, String name)
    {
        try {
            switch (name)
            {
                case "Pistolet":
                    bulletImage = ImageIO.read(new File("./res/bullet/pistolet-bullet.png"));
                    break;
                case "Mitrailleuse":
                    bulletImage = ImageIO.read(new File("./res/bullet/mitrailleuse-bullet.png"));
                    break;
                case "Sniper":
                    bulletImage = ImageIO.read(new File("./res/bullet/sniper-bullet.png"));
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (this.isActive()) 
        {
            // Save the current graphics transform
            AffineTransform originalTransform = ((Graphics2D) g).getTransform();

            // Rotate the image
            AffineTransform rotationTransform = AffineTransform.getTranslateInstance(x, y + 16);
            
            double rotationAngle = 0.0; // Default to no rotation

            switch (this.orientation)
            {
                case UP:
                    rotationAngle = -Math.PI / 2.0; // -90 degrees in radians
                    break;
                case DOWN:
                    rotationAngle = Math.PI / 2.0; // 90 degrees in radians
                    break;
                case LEFT:
                    rotationAngle = Math.PI; // 180 degrees in radians
                    break;
                case RIGHT:
                    // No rotation for RIGHT (default orientation)
                    break;
                default:
                    break;
            }
            rotationTransform.rotate(rotationAngle);
            ((Graphics2D) g).setTransform(rotationTransform);

            // Draw the rotated image
            g.drawImage(bulletImage, -8, -8, 16, 16, null);

            // Restore the original graphics transform
            ((Graphics2D) g).setTransform(originalTransform);
        }
    }
}
