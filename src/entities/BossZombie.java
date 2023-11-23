package entities;

public class BossZombie extends Zombie
{
    private static int idBossZ = 1;

    private int upgrade = 0;

    public BossZombie()
    {
        super("BossZombie #" + idBossZ, 500, 100, 5, 50);
        idBossZ++;
    }

    @Override
    public boolean receiveDamage(int damage)
    {
        if (super.receiveDamage(damage))
        {
            if (super.getHp() < 150 && upgrade == 1)
            {
                this.upgrade();
            }
            else if (super.getHp() < 250 && upgrade == 0)
            {
                this.upgrade();
            }

            return true;
        }
        else return false;
    }

    public void upgrade()
    {
        if (this.upgrade == 0)
        {
            this.upgrade++;

            // diminution des degats + augmentation de la speed
            super.force /= 2;
            super.speed *= 2;
            System.out.println("BossZombie has Upgrade #1");
            System.out.println("BossZombie stats : HP = " + super.getHp() + ", Speed = " + super.getSpeed());
        }
        else if (this.upgrade == 1)
        {
            this.upgrade++;

            // diminution des degats + augmentation de la speed
            super.force /= 2;
            super.speed *= 2;
            System.out.println("BossZombie has Upgrade #2");
            System.out.println("BossZombie stats : HP = " + super.getHp() + ", Speed = " + super.getSpeed());
        }
    }
}
