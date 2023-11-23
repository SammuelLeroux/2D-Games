package entities;

public class MiniZombie extends Zombie
{
    private static int idMiniZ = 1;

    public MiniZombie()
    {
        super("MiniZombie #" + idMiniZ, 20, 10, 10, 10);
        idMiniZ++;
    }
}
