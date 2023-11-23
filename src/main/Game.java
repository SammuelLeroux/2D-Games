package main;

import java.util.List;
import java.util.ArrayList;

import javax.swing.*;
import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import java.io.*;

// thread
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import entities.*;
import level.*;
import weapons.Weapon;

import static level.Level.weaponList;

import static utilz.Constants.Directions.*;

// menu
import menu.GunSelectionMenu;
import menu.Menu;
import menu.PauseMenu;
import menu.Shop;
import utilz.Constants.Screen;
import menu.GameCompleted;

// musique
import javax.sound.sampled.*;

public class Game implements Runnable, ActionListener, ImageObserver
{
	private GamePanel gamePanel;
	private Thread gameThread;

	private JPanel pauseMenu;

	// musique
	private Clip backgroundMusicClip;

	// pour la sauvegarde
	private GameData gameData;

	// pour la map
	private ProceduralMap proceduralMap;

	private final int FPS_SET = 120;
	private final int UPS_SET = 200;
	private static final int DELAY_BETWEEN_ZOMBIE_MOVES = 500;

	// joueur
	private Player player;
	private Gandalf gandalf;

	// zombies
    public static List<Zombie> zombieList = new ArrayList<>();

	private static int level;
	public static enum ZoneType { SAFEZONE, NOMENSLAND }
	public static ZoneType currentZoneType = ZoneType.NOMENSLAND;

    private static Level stage;
	private SafeZone safeZone;
	private NoMensLand noMensLand = new NoMensLand(level);

	// pour permettre le switch d'arme entre celle choisi et le pistolet
	private Weapon stageWeapon;

	// etat du jeu
	public static enum State { PLAY, WAITING, PAUSE, OPENSHOP };
	public static State state = State.PLAY;

	public Player getPlayer() { return this.player; }
	public ZoneType getCurrentZoneType() { return currentZoneType; }
	public State getState() { return state; }
	public Level getStage() { return stage; }
	public Weapon getStageWeapon() { return this.stageWeapon; }
	public GameData getGameData() { return this.gameData; }

	public void setState(State etat) { state = etat; }
	public void setCurrentZoneType(ZoneType zone) { currentZoneType = zone; }

	public Game()
	{
		gamePanel = new GamePanel(this);
		new GameWindow(gamePanel);
		gamePanel.requestFocus();
		
		// initialisation du joueur, pistolet et des zombie du niveau 1
        init();

		startGameLoop();

		// Rendre la fenêtre visible
        gamePanel.setVisible(true);
		initPauseMenu();
	}
	
	private void initPauseMenu() {
        ActionListener resumeListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resume();
            }
        };

        ActionListener restartListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restart();
            }
        };

        ActionListener quitListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitter();
            }
        };

        pauseMenu = new PauseMenu(resumeListener, restartListener, quitListener);
        pauseMenu.setVisible(false);
        gamePanel.add(pauseMenu);
    }
	
    private void restart()
	{
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				// Fermer la fenêtre du jeu actuelle
				JFrame currentFrame = (JFrame) gamePanel.getTopLevelAncestor();
				currentFrame.dispose();

				// Relancer le jeu en créant une nouvelle instance de Menu (ou de la classe principale)
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						new Menu();
					}
				});
			}
		});
	}
		
    public void resume()
	{
        state = State.PLAY;

        // Allow the player to move
        player.setCanMove(true);

        // Restart the zombies
        moveZombie(player);

        // Hide the pause menu
        pauseMenu.setVisible(false);
    }

	private void init()
    {
		state = State.WAITING;

		// on vide les listes 
        zombieList.clear();
		weaponList.clear();

		level = 1;

		this.player = new Player(Screen.WIDTH / 2, Screen.HEIGHT / 2);

		safeZone = new SafeZone(player);
		noMensLand = new NoMensLand(level);

		stage = noMensLand;

		// map
		this.proceduralMap = stage.getProceduralMap();

		// choix des armes
		GunSelectionMenu gunSelected = new GunSelectionMenu(weaponList);
		gunSelected.setGunSelectionCallback(selectedGun -> {

			for (Weapon weapon : weaponList)
			{
				if (weapon.getName().equals(gunSelected.getSelectedGun()))
				{
					this.player.equip(weapon);
					// pour permettre le switch d'arme entre celle choisi et le pistolet
					this.stageWeapon = weapon;
				}
			}

			zombieList.addAll(((NoMensLand) stage).getEnemiesLevel());
			
			// Add a delay to ensure zombies are activated before creating GamePanel
			try {
				Thread.sleep(5000); // Adjust the delay as needed
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			state = State.PLAY;

			// Deplacement des zombies via un autre Thread
			if (zombieList.size() > 0 ) moveZombie(player);
			// gameData = new GameData(player, stage);
		});
    }

	public void updateLevel()
    {
		// on enleve les zombies de la liste si il y en a
        zombieList.clear();

		proceduralMap.viderObstaclesList();

		// for (Weapon weapon : weaponList) { System.out.println(weapon.getName() + ": " + weapon.getBarrel()); }

		state = State.WAITING;

		// on sauvegarde
		gameData = new GameData(player, stage);
		// this.save();

		if (currentZoneType == ZoneType.NOMENSLAND)
		{
			// safezone
			System.out.println("Entered Safe ZONE");
			currentZoneType = ZoneType.SAFEZONE;

			changeBackgroundMusic("./res/audio/menu.wav");
			
			stage = safeZone;

			// generer map
			this.proceduralMap = safeZone.getProceduralMap();

			// si il est est pres du gandalf, il peut cliquer sur lui et ouvrir le shop
			System.out.println("Gandalf: Bonjour, la boutique est disponible");
			System.out.println("Player: Bonjour gandalf");
			
			// gandalf
			this.gandalf = safeZone.getGandalf();

			// on place le player
			this.player.setX(Screen.WIDTH / 2); 
			this.player.setY(Screen.HEIGHT / 2 + 5 * Screen.spriteHeight);
			Player.orientation = UP;
			this.player.setCanAttack(false);

			state = State.PLAY;

			// le perso peut bouger
			player.setCanMove(true);
		}
		else if (currentZoneType == ZoneType.SAFEZONE)
		{
			// on augmente le level
			level++;
			noMensLand.setLevel(level);

			// nomensland
			System.out.println("LET'S BEAT THESE ZOMBIES");
			currentZoneType = ZoneType.NOMENSLAND;

			changeBackgroundMusic("./res/audio/HorrorSound.wav");

			stage = noMensLand;

			// generer map
			this.proceduralMap = noMensLand.getProceduralMap();

			// on recupere les zombies
			zombieList.addAll(noMensLand.getEnemiesLevel());

			// on place le player
			this.player.setX(Screen.WIDTH / 2); 
			this.player.setY(Screen.HEIGHT / 2);
			this.player.setCanMove(false);

			GunSelectionMenu gunSelected = new GunSelectionMenu(weaponList);
			gunSelected.setGunSelectionCallback(selectedGun -> {

				for (Weapon weapon : weaponList)
				{
					if (weapon.getName().equals(gunSelected.getSelectedGun()))
					{
						this.player.equip(weapon);

						// pour permettre le switch d'arme entre celle choisi et le pistolet
						this.stageWeapon = weapon;
					}
				}

				state = State.PLAY;

				// le perso peut bouger
				player.setCanMove(true);

				this.player.setCanAttack(true);

				// Add a delay to ensure zombies are activated before creating GamePanel
				try {
					Thread.sleep(1000); // Adjust the delay as needed
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// Deplacement des zombies via un autre Thread
				moveZombie(player);
			});
		}
    }

	public void moveZombie(Player target)
    {
		if (zombieList.size() > 0)
		{
			for (Zombie zombie : zombieList)
			{
				zombie.setTarget(target);
			}
	
			// Utiliser ExecutorService pour exécuter les zombies en parallèle
			ExecutorService executorService = Executors.newFixedThreadPool(zombieList.size());
	
			for (Zombie zombie : zombieList)
			{
				executorService.submit(() -> {
					while (zombie.getHp() > 0)
					{
						zombie.moveTowards();
						SwingUtilities.invokeLater(() -> {
							// Mettez à jour l'affichage après chaque déplacement du zombie
							gamePanel.repaint();
						});
	
						try {
							// Ajoutez une pause pour voir l'avancement en temps réel
							Thread.sleep(DELAY_BETWEEN_ZOMBIE_MOVES); // ajustez la durée de pause selon vos besoins
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
	
					if (zombie.getHp() <= 0) zombieList.remove(zombie);
				});
			}
			
			// Arrêter le pool de threads après que toutes les tâches ont été soumises
			executorService.shutdown();
	
			// Add a delay to ensure zombies are activated before creating GamePanel
			try {
				// executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }

	public void pause()
    {
		state = State.PAUSE;

		// on stop les zombie
        for (Zombie zombie : zombieList)
        {
            zombie.setTarget(null);
        }

		// on stop le perso
		player.setCanMove(false);
		pauseMenu.setVisible(true);
	}

	public void reprendre()
	{
		state = State.PLAY;

		pauseMenu.setVisible(false);

		// on peut rebouger le perso
		player.setCanMove(true);

		// on relance les zombies
		moveZombie(player);
	}

	public void openShop()
	{
		// ouverture du shop
		state = State.OPENSHOP;

		new Shop(gamePanel, stage.getBonusList());
	}

	public void closeShop()
	{
		// update le level
		updateLevel();
	}

	public void quitter()
	{
		// on ferme la fenetre
		System.exit(0);
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update()
	{
		player.update();
	}

	public void render(Graphics g) 
	{
		// map
		g.drawImage(proceduralMap.getMap(), 0, 0, this);
		
		// player
		if (player != null)
		{
			player.render(g);

			// Dessiner le compteur de pièces
			Image goldImage = goldImage();
			if (goldImage != null) g.drawImage(goldImage, Screen.WIDTH - 225, Screen.HEIGHT - 100, this);
			
			g.setColor(Color.BLACK);
			Font font = new Font("Arial", Font.BOLD, 24);
            g.setFont(font);
			g.drawString("Pièces: " + player.getGold(), Screen.WIDTH - 170, Screen.HEIGHT - 70);

			// barre de vie et golds
			g.drawImage(barreVieImage(), 50, Screen.HEIGHT - 125, this);
		}
		
		// Dessiner les zombies
		if (currentZoneType == ZoneType.NOMENSLAND)
		{
			for (Zombie zombie : zombieList)
			{
				if (zombie != null && zombie.getHp() > 0) g.drawImage(zombie.getZombieImage(), (int) zombie.getX(), (int) zombie.getY(), 64, 64, this);
			}
		}

		// gandalf dans la safe zone
		if (currentZoneType == ZoneType.SAFEZONE)
		{
			if (gandalf != null) g.drawImage(gandalf.getGandalfImage(), (int) gandalf.getX(), (int) gandalf.getY(), 64, 64, this);
		}
	}

	private Image barreVieImage()
	{
		Image imageVie;
		if (player.getHp() == 100)
		{
			imageVie = new ImageIcon("./res/infoScore/life/life100.png").getImage();
		}
		else if (player.getHp() < 100 && player.getHp() >= 75)
		{
			imageVie = new ImageIcon("./res/infoScore/life/life75.png").getImage();
		}
		else if (player.getHp() < 75 && player.getHp() >= 50)
		{
			imageVie = new ImageIcon("./res/infoScore/life/life50.png").getImage();
		}
		else if (player.getHp() < 50 && player.getHp() >= 25)
		{
			imageVie = new ImageIcon("./res/infoScore/life/life25.png").getImage();
		}
		else if (player.getHp() < 25 && player.getHp() >= 15)
		{
			imageVie = new ImageIcon("./res/infoScore/life/life15.png").getImage();
		}
		else
		{
			imageVie = new ImageIcon("./res/infoScore/life/life0.png").getImage();
		}

		return imageVie;
	}

	private Image goldImage()
	{
		// Réduire la taille de l'image représentant les pièces
		try {
			BufferedImage originalImage = ImageIO.read(new File("./res/infoScore/coins.png"));
			Image resizedImagePiece = originalImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			return resizedImagePiece;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void save() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("save"))) {
            out.writeObject(gameData);
            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void run()
	{

		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;
		long previousTime = System.nanoTime();

		int updates = 0;
		int frames = 0;

		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;

		// Démarrez la lecture de la musique de fond initiale
        playBackgroundMusic();

		while (true)
		{
			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1)
			{
				update();
				updates++;
				deltaU--;				
			}

			if (deltaF >= 1)
			{
				gamePanel.repaint();
				deltaF--;
				frames++;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000)
			{
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + "| UPS: " + updates);
				frames = 0;
				updates = 0;
			}

			// si l'arme choisie par le perso n'a plus de balle -> on equip le pistolet
			if (player != null && player.getEquipedWeapon() != null && !player.getEquipedWeapon().getName().equals("Pistolet") && player.getEquipedWeapon().getBarrel() == 0)
			{
				for (Weapon weapon : weaponList)
				{
					if (weapon.getName().equals("Pistolet"))
					{
						this.player.equip(weapon);
						this.stageWeapon = weapon;
					}
				}
			}

			if (currentZoneType == ZoneType.SAFEZONE)
			{
				// interaction avec le gandalf

				if (gandalf != null)
				{
					if (player.nearShop(gandalf))
					{
						// ouverture du shop
						state = State.OPENSHOP;
					}
					else
					{
						state = State.PLAY;
					}
				}
			}
			else if (currentZoneType == ZoneType.NOMENSLAND)
			{
				
			}

			// mise a jour des levels
			if (currentZoneType == ZoneType.NOMENSLAND && zombieList.size() == 0 && state == State.PLAY)
			{
				// le perso ne peut pas bouger jusqu'au chargement du stage suivant
				player.setCanMove(false);

				if (level < 5)
				{
					updateLevel();
				}
				else
				{
					// success
					System.out.println("BRAVO !!!");
					
					// supprimer la sauvegarde -> car il a fini le jeu donc pas possible de reprendre à la derniere sauvegarde

					// afficher fenetre de succes
					new GameCompleted();
				}

			}
		}
	}

	@Override
    public void actionPerformed(ActionEvent e)
	{
        // Update game state
        this.player.update(); // Update player state
        for (Zombie zombie : zombieList) {
            zombie.moveTowards(); // Update zombie state
        }

        // Repaint the panel
        gamePanel.repaint();
    }

	public void windowsFocusLost() {
		player.resetDirBooleans();
	}

	public void changeBackgroundMusic(String soundFile) {
        // Arrêtez la musique de fond actuelle
        stopBackgroundMusic();

        // Démarrez la nouvelle musique de fond
        playBackgroundMusic(soundFile);
    }

	public void playBackgroundMusic() {
        // Votre musique de fond initiale
        playBackgroundMusic("./res/audio/HorrorSound.wav");
    }
	public void playBackgroundMusic(String soundFile) {
		Thread musicThread = new Thread(() -> {
			try {
				File audioFile = new File(soundFile).getAbsoluteFile();
				if (!audioFile.exists()) {
					System.out.println("Le fichier audio n'existe pas : " + soundFile);
					return;
				}
	
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
				backgroundMusicClip = AudioSystem.getClip();
				backgroundMusicClip.open(audioInputStream);
				backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY); // Bouclez la musique en continu
				backgroundMusicClip.start();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
				System.out.println("Erreur lors de la lecture de la musique de fond.");
				ex.printStackTrace();
			} catch (Exception ex) {
				System.out.println("Une erreur inattendue s'est produite lors de la lecture de la musique de fond.");
				ex.printStackTrace();
			}
		});
	
		// Définissez le thread en tant que thread de fond pour qu'il se termine lorsque le programme principal se termine
		musicThread.setDaemon(true);
	
		// Démarrez le thread de musique de fond
		musicThread.start();
	}

	public void stopBackgroundMusic() {
        // Arrêtez la musique de fond actuelle s'il y en a une en cours de lecture
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close(); // Fermez la ressource audio
        }
    }

	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'imageUpdate'");
	}
}