package level;

import java.util.List;
import java.util.ArrayList;

import entities.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.File;

public class NoMensLand extends Level
{
    // zombies des levels
    private List<Zombie> enemiesLevel = new ArrayList<>();

    public List<Zombie> getEnemiesLevel()
    {
        this.enemiesLevel = generateEnemies(super.getLevel());
        return this.enemiesLevel;
    }

    public NoMensLand(int level)
    {
        // constructeur pour les niveaux de combats
        super(level);
    }

    public Level getStage()
    {
        return super.noMensLand = new NoMensLand(super.level);
    }

    private static List<Zombie> generateEnemies(int level)
    {
        List<Zombie> enemies = new ArrayList<>();

        // generer les zombies en fonction du niveau
        switch (level)
        {
            case 1:
                // 10 mini
                for (int i = 0; i < 10; i++)
                {
                    MiniZombie miniZombie1 = new MiniZombie();
                    enemies.add(miniZombie1);
                }
                break;
            case 2:
                // 5 goules
                for (int i = 0; i < 5; i++)
                {
                    Goule goule2 = new Goule();
                    enemies.add(goule2);
                }
                break;
            case 3:
                // 5 mini & 3 goules
                for (int i = 0; i < 5; i++)
                {
                    MiniZombie miniZombie3 = new MiniZombie();
                    enemies.add(miniZombie3);
                    if (i < 3)
                    {
                        Goule goule3 = new Goule();
                        enemies.add(goule3);
                    }
                }
                break;
            case 4:
                // 5 mini, 3 goules & 2 mega
                for (int i = 0; i < 5; i++)
                {
                    MiniZombie miniZombie3 = new MiniZombie();
                    enemies.add(miniZombie3);
                    if (i < 3)
                    {
                        Goule goule3 = new Goule();
                        enemies.add(goule3);
                    }
                    
                    if (i < 2)
                    {
                        MegaZombie megaZombie3 = new MegaZombie();
                        enemies.add(megaZombie3);
                    }
                }
                break;
            case 5:
                // 5 mini
                for (int i = 0; i < 5; i++)
                {
                    MiniZombie miniZombie1 = new MiniZombie();
                    enemies.add(miniZombie1);

                    if (i < 1)
                    {
                        // 1 boss
                        BossZombie boss = new BossZombie();
                        enemies.add(boss);
                    }
                }
                break;
            default:
                break;
        }

        return enemies;
    }

    public void playBackgroundMusic(String musicFile) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(musicFile).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch(UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error with playing sound.");
            e.printStackTrace();
        }
    }
}
