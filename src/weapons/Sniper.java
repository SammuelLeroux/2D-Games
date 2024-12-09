package weapons;

import static utilz.Constants.Screen.*;

public class Sniper extends Weapon
{
    private long lastFireTime; // temps du dernier tir
    private long fireRate; // cadence de tir en ms
    
    public long getLastFireTime() { return this.lastFireTime; }
    public long getFireRate() { return this.fireRate; }

    public Sniper()
    {
        // sniper coup par coup -> one shot
        super("Sniper", 100, WIDTH, 1, 5);
        super.barrel = 5;
        this.fireRate = 5000;
        this.lastFireTime = 0;
    }

    @Override
    protected Bullet createBullet(int orientation, float start, float end, float axeConstant, int bulletSpeed)
    {
        return new Bullet(orientation, start, end, axeConstant, bulletSpeed);
    }

    @Override
    public boolean fire(int orientation, float start, float end, float axeConstant, int bulletSpeed)
    {
        if (super.nbBullet <= 0)
        {
            // on recharge si le chargeur est vide
            this.reloadWeapon(1);

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
