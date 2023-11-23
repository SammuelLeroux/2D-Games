package tools;

import entities.Player;

public class Potion extends Bonus
{
    public Potion()
    {
        super("Potion", 50);
    }

     public void usePotion(Player player)
    {
        System.out.println("Player'hp: " + player.getHp());
        System.out.println("use Potion: " + super.usedBonus);
        if (super.usedBonus == false)
        {
            // le player peut recuperer 50% de ses points de vie manquants
            // player.recoverHp((int) ((100 - player.getHp()) * 0.5));
            player.recoverHp(100);
        }
        System.out.println("Player'hp: " + player.getHp());
    }

    @Override
    public void useBonus(Player player)
    {
        usePotion(player);
        super.useBonus(player);
    }
}
