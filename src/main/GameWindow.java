package main;

import static utilz.Constants.Screen.*;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

public class GameWindow {
	private JFrame jframe;

	public GameWindow(GamePanel gamePanel)
	{

		jframe = new JFrame();

		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Calculate the center position for GamePanel
        int screenWidth = jframe.getToolkit().getScreenSize().width;
        int screenHeight = jframe.getToolkit().getScreenSize().height;

        int centerX = (screenWidth - WIDTH) / 2;
        int centerY = (screenHeight - HEIGHT) / 2;

		// Set the bounds for GamePanel
        gamePanel.setBounds(centerX, centerY, WIDTH, HEIGHT);

		jframe.add(gamePanel);
		jframe.setSize(WIDTH, HEIGHT); // Set the size explicitly
		jframe.setLocationRelativeTo(null);
		jframe.setResizable(true);
		// jframe.pack();
		jframe.setVisible(true);

		jframe.addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().windowsFocusLost();
			}

			@Override
			public void windowGainedFocus(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

}