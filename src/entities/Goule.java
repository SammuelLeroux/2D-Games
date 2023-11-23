package entities;

public class Goule extends Zombie
{
    private static int idGoule = 1;

    public Goule()
    {
        super("Goule #" + idGoule, 10, 5, 15, 5);
        idGoule++;
    }
}
