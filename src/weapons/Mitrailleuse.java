package weapons;

public class Mitrailleuse extends Weapon
{
    private long lastFireTime; // temps du dernier tir
    private long fireRate; // cadence de tir en ms
    
    public long getLastFireTime() { return this.lastFireTime; }
    public long getFireRate() { return this.fireRate; }

    public Mitrailleuse()
    {
        // mitrailleuse 3 coups
        super("Mitrailleuse", 30, 32 * 10, 9, 2);
        super.barrel = 3; // mitrailleuse a 3 chargeurs de 9 balles
        this.fireRate = 1000;
        this.lastFireTime = 0;
    }

    @Override
    protected Bullet createBullet(int orientation, float start, float end, float axeConstant, int bulletSpeed)
    {
        return new Bullet(orientation, start, end, axeConstant, bulletSpeed);
    }

    @Override
    public void reloadWeapon(int valeur)
    {
        if (super.barrel > 0 && super.nbBullet <= 0)
        {
            // on recharge le nombre de balle
            super.nbBullet += valeur;
            // on enleve un chargeur
            super.barrel--;
        }
    }

    @Override
    public boolean fire(int orientation, float start, float end, float axeConstant, int bulletSpeed)
    {
        if (super.nbBullet <= 0)
        {
            // on recharge si le chargeur est vide
            this.reloadWeapon(9);
            return false;
        }
        else
        {
            long currentTime = System.currentTimeMillis();

            if (currentTime - this.lastFireTime > this.fireRate)
            {
                if (super.fire(orientation, start, end, axeConstant, bulletSpeed))
                {
                    // mettre à jour le temps du dernier tir
                    this.lastFireTime = currentTime;

                    return true;
                }
                else return false;

            }
            else
            {
                return false;
            }
        }
    }
}