package weapons;

public class Pistolet extends Weapon
{
    private long lastFireTime; // temps du dernier tir
    private long fireRate; // cadence de tir en ms
    
    public long getLastFireTime() { return this.lastFireTime; }
    public long getFireRate() { return this.fireRate; }

    public Pistolet()
    {
        super("Pistolet", 10, 32 * 5, 10, 1);
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
        if (super.nbBullet <= 0) super.nbBullet += valeur;
    }

    @Override
    public boolean fire(int orientation, float start, float end, float axeConstant, int bulletSpeed)
    {
        if (super.nbBullet <= 0)
        {
            // on recharge si le chargeur est vide
            this.reloadWeapon(10);
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
