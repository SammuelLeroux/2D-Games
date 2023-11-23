package entities;

public abstract class Entity
{
    protected int hp;
    protected float x,y;
    protected float speed;
    
    public Entity(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
    public Entity(int hp, float speed, float x, float y)
    {
        this.hp = hp;
        this.speed = speed;
        this.x = x;
        this.y = y;
    }

    public int getHp() { return this.hp; }
    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public float getSpeed() { return this.speed; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    
    public boolean receiveDamage(int damage)
    {
        if (this.hp <= damage)
        {
            this.hp = 0;
        }
        else
        {
            this.hp -= damage;
            System.out.println("Hp " + this.getClass().getName() + ": " + this.hp);
        }

        return true;
    }
}
