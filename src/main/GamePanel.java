package main;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import static utilz.Constants.Screen;

public class GamePanel extends JPanel
{
	private MouseInputs mouseInputs;
	private Game game;

	public Game getGame() { return this.game; }

	public GamePanel(Game game)
	{
		this.game = game;

		// panel
		setPanelSize();

		// inputs
		mouseInputs = new MouseInputs(this);
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
		
		setFocusable(true);
        requestFocusInWindow();
	}

	private void setPanelSize() {
		//      Dimension size = new Dimension(1280, 720);
		Dimension size = new Dimension(Screen.WIDTH, Screen.HEIGHT);
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
	}

	public void updateGame() {

	}
 
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}
}