package utilz;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Constants {

    public static class Directions
    {
        public static final int DOWN = 0;
        public static final int LEFT = 1;
        public static final int RIGHT = 2;
        public static final int UP = 3;
    }

    public static Dimension getScreenSize()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        return toolkit.getScreenSize();
    }

    public static boolean Range(float value, float start, float end)
    {
        if (value >= start && value <= end) return true;
        else return false;
    }

    public static class Screen
    {
        public static final int spriteWidth = 32;
        public static final int spriteHeight = 32;

        public static final Dimension SCREEN_SIZE = getScreenSize();
        public static final int WIDTH = SCREEN_SIZE.width - spriteWidth * 2;
        public static final int HEIGHT = SCREEN_SIZE.height - spriteHeight;
        public static final int obstacleSize = (int) (Screen.spriteHeight * 1.5);

        //private static final int WIDTH = 1280;
        //private static final int HEIGHT = 720;

        // public static final int WIDTH = 1280 - spriteWidth;
        // public static final int HEIGHT = 720 - spriteHeight;
    }

    public static class PlayerConstants
    {
        public static final int RUNNING = 2;
        public static final int TEST2 = 1;
        public static final int TEST3 = 9;
        public static final int TEST4 = 99;
        public static final int ATTACK_1 = 6;
        public static final int ATTACK_JUMP_1 = 7;
        public static final int ATTACK_JUMP_2 = 8;
        public static final int IDLE = 0;

        public static int GetSpriteAmount(int player_action)
        {
            switch (player_action) 
            {
                case IDLE:
                    return 1;
                case RUNNING:
                case TEST2:
                case TEST3:
                case TEST4:
                    return 3;

                default:
                    return 1;
            }
        }
    }
    
}
