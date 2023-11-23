package tools;

import entities.Player;
import weapons.*;

import static level.Level.weaponList;

public class MunitionMax extends Bonus
{
    public MunitionMax()
    {
        super("MunitionMax", 100);
    }

    public void reloadAllMunitions()
    {
        // on vide l'ancien slot d'arme
        weaponList.clear();

        // on remet un slot d'arme neuve
        weaponList.add(new Pistolet());
        weaponList.add(new Mitrailleuse());
        weaponList.add(new Sniper());
    }

    @Override
    public void useBonus(Player player)
    {
        this.reloadAllMunitions();
        super.useBonus(player);
    }
}
