package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;
import weapons.Weapon;

// etat
import static main.Game.State;

import static level.Level.weaponList;

public class KeyboardInputs implements KeyListener {

	private GamePanel gamePanel;

	public KeyboardInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_Z:
				gamePanel.getGame().getPlayer().setUp(false);
				break;
			case KeyEvent.VK_Q:
				gamePanel.getGame().getPlayer().setLeft(false);
				break;
			case KeyEvent.VK_S:
				gamePanel.getGame().getPlayer().setDown(false);
				break;
			case KeyEvent.VK_D:
				gamePanel.getGame().getPlayer().setRight(false);
				break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_Z:
				if (gamePanel.getGame().getPlayer().canMove()) gamePanel.getGame().getPlayer().setUp(true);
				break;
			case KeyEvent.VK_Q:
				if (gamePanel.getGame().getPlayer().canMove()) gamePanel.getGame().getPlayer().setLeft(true);
				break;
			case KeyEvent.VK_S:
				if (gamePanel.getGame().getPlayer().canMove()) gamePanel.getGame().getPlayer().setDown(true);
				break;
			case KeyEvent.VK_D:
				if (gamePanel.getGame().getPlayer().canMove()) gamePanel.getGame().getPlayer().setRight(true);
				break;
			case KeyEvent.VK_SPACE:
				// Systeme de switch d'arme entre l'arme choisi pour le niveau et le pistolet
				if (gamePanel.getGame().getState() == State.PLAY && gamePanel.getGame().getPlayer().canAttack())
				{
					if (!gamePanel.getGame().getStageWeapon().getName().equals("Pistolet"))
					{
						// l'arme choisi pour le niveau n'est pas le pistolet

						// si le player est equipe du pistolet et que l'arme qu'il avait choisi pour le niveau a encore des balles : on lui remet l'arme qu'il avait choisi pour le niveau
						if (gamePanel.getGame().getPlayer().getEquipedWeapon().getName().equals("Pistolet") && gamePanel.getGame().getStageWeapon().getBarrel() > 0)
						{
							for (Weapon weapon : weaponList)
							{
								if (weapon.getName().equals(gamePanel.getGame().getStageWeapon().getName())) gamePanel.getGame().getPlayer().equip(weapon);
							}
						}
						else
						{
							// on l'equipe du pistolet
							for (Weapon weapon : weaponList)
							{
								if (weapon.getName().equals("Pistolet")) gamePanel.getGame().getPlayer().equip(weapon);
							}
						}
					}
				}
				break;
			case KeyEvent.VK_ESCAPE:
				if (gamePanel.getGame().getState() != State.PAUSE) gamePanel.getGame().pause();
				else gamePanel.getGame().reprendre();
				break;
			case KeyEvent.VK_ENTER:
				if (gamePanel.getGame().getState() == State.PAUSE) gamePanel.getGame().reprendre();
				break;
		}
	}
}