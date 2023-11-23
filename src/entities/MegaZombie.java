package entities;

public class MegaZombie extends Zombie
{
    private static int idMegaZ = 1;

    public MegaZombie()
    {
        super("MegaZombie #" + idMegaZ, 50, 50, 5, 25);
        idMegaZ++;
    }
}
