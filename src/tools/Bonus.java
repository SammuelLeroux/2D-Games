package tools;

import entities.Player;

public class Bonus implements Tools
{
    protected String name;
    protected int coutBonus;
    protected boolean usedBonus;

    public String getName() { return name; }
    public int getCoutBonus() { return this.coutBonus; }

    public Bonus(String name, int cout)
    {
        this.name = name;
        this.coutBonus = cout;
    }

    @Override
    public boolean isUsed() {
        return this.usedBonus;
    }

    @Override
    public void setUsed(boolean usedBonus) {
        this.usedBonus = usedBonus;
    }

    @Override
    public void useBonus(Player player) {
        setUsed(true);
    }
}