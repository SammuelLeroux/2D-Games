package main;

import java.util.Random;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.JFrame;

import static utilz.Constants.Screen;

import static main.Game.ZoneType;

public class ProceduralMap extends JFrame
{
    private BufferedImage map;
    private ZoneType zoneType;
    public static float[][] obstaclesList = new float[20][2];

    private BufferedImage blockImage1; // Ajout de la variable pour l'image du bloc
    private BufferedImage blockImage2;
    private BufferedImage blockImage3;

    public BufferedImage getMap() {
        return map;
    }

    public ProceduralMap(ZoneType zoneType)
    {
        try {
            blockImage1 = ImageIO.read(new File("./res/obstacles/bloc1.jpg"));
            blockImage2 = ImageIO.read(new File("./res/obstacles/bloc2.jpg"));
            blockImage3 = ImageIO.read(new File("./res/obstacles/bloc3.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.zoneType = zoneType;

        map = generateProceduralMap();
    }

    public BufferedImage getRandomBlockImage()
    {
        BufferedImage[] blockImages = {blockImage1, blockImage2, blockImage3};
        Random random = new Random();
        int randomIndex = random.nextInt(blockImages.length);
        return blockImages[randomIndex];
    }

    public BufferedImage generateProceduralMap()
    {
        BufferedImage image = new BufferedImage(Screen.WIDTH, Screen.HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        Random random = new Random();

        // Couleur de fond
        if (this.zoneType == ZoneType.NOMENSLAND)
        {
            g.setColor(Color.decode("#2F4F4F"));
        }
        else
        {
            g.setColor(Color.decode("#696969"));
        }

        g.fillRect(0, 0, Screen.WIDTH, Screen.HEIGHT);

        if (this.zoneType == ZoneType.NOMENSLAND)
        {
            // obstacles pour la zone de combat

            // obstacles aléatoire
            g.setColor(Color.decode("#1E1E1E"));
            
            BufferedImage blocImage = this.getRandomBlockImage();
            for (int i = 0; i < 20; i++)
            {
                float x = random.nextInt(Screen.WIDTH - Screen.obstacleSize);
                float y = random.nextInt(Screen.HEIGHT - 125 - Screen.obstacleSize); // - 125 pour ne as avoir d'obstacle sur l'espace pour la barre de vie

                while (x == Screen.WIDTH / 2 && y == Screen.HEIGHT / 2)
                {
                    // pas d'obstacle sur l'endroit ou est le joueur
                    x = random.nextInt(Screen.WIDTH - Screen.obstacleSize);
                    y = random.nextInt(Screen.HEIGHT - 125 - Screen.obstacleSize);
                }

                // on stocke la position de l'obstacle
                float[] position = new float[]{x, y};

                obstaclesList[i] = position;

                g.drawImage(blocImage, (int) x, (int) y, Screen.obstacleSize, Screen.obstacleSize, null);
            }
        }

        return image;
    }

    public void viderObstaclesList()
    {
        for (int i = 0; i < obstaclesList.length; i++) {
            for (int j = 0; j < obstaclesList[i].length; j++) {
                // Initialisation à la valeur par défaut (0.0f dans ce cas)
                obstaclesList[i][j] = 0.0f;
            }
        }
    }
}