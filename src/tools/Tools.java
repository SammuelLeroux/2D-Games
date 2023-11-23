package tools;

import entities.Player;

public interface Tools
{
    public boolean isUsed();

    public void setUsed(boolean used);

    public void useBonus(Player player);
}
