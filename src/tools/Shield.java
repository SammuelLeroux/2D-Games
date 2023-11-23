package tools;

public class Shield extends Bonus
{
    private int shieldPoint;

    public int getShieldPoint() { return this.shieldPoint; }

    public Shield()
    {
        super("Shield", 100);
        this.shieldPoint = 50;
    }

    public boolean receiveDamage(int damage)
    {
        if (usedBonus == false)
        {
            if (damage >= shieldPoint)
            {
                shieldPoint = 0;
                System.out.println("Player a perdu son shield!");

                // le bonus a ete utilise, il ne peut plus l'etre
                usedBonus = true;

                return false;
            }
            else
            {
                shieldPoint -= damage;
                System.out.println("Il reste: " + shieldPoint + " points de shield.");

                return true;
            }
        }
        return false;
    }
}
