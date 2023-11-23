package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import main.GamePanel;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.io.File;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

// type de zone
import static main.Game.ZoneType;

// etat
import static main.Game.State;

public class MouseInputs implements MouseListener, MouseMotionListener {

	private GamePanel gamePanel;
	public MouseInputs(GamePanel gamePanel) {
		this.gamePanel= gamePanel;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		if (gamePanel.getGame().getPlayer().canMove() && gamePanel.getGame().getPlayer().canAttack()) gamePanel.getGame().getPlayer().attack();

		if (gamePanel.getGame().getState() == State.OPENSHOP && gamePanel.getGame().getCurrentZoneType() == ZoneType.SAFEZONE) gamePanel.getGame().openShop();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void playSound(String soundFile)
	{
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