package level;

import entities.Gandalf;
import entities.Player;

public class SafeZone extends Level
{
    private Gandalf gandalf;
    private Player player;
    
    public SafeZone(Player player)
    {
        super();
        this.player = player;
        this.gandalf = new Gandalf(player);
    }

    public Gandalf getGandalf()
    {
        return this.gandalf;
    }

    @Override
    public Level getStage()
    {
        return super.safeZone = new SafeZone(player);
    }

    public Player getPlayer() { return this.player; }
}
