package weapons;

public interface WeaponAction
{
    public void reloadWeapon(int value);
    public boolean fire(int orientation, float start, float end, float axeConstant, int bulletSpeed);
}
