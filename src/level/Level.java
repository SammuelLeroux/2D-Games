package level;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;

import tools.*;
import weapons.*;
import main.Game.ZoneType;
import main.ProceduralMap;

public abstract class Level implements Serializable
{
    // info partie
    protected int level;
    private ZoneType zone;
    protected SafeZone safeZone;
    protected NoMensLand noMensLand;

    // map
    // private ProceduralMap proceduralMap;

    // bonus
    protected static List<Bonus> bonus = new ArrayList<>();
    private Potion potion = new Potion();
    private Shield shield = new Shield();
    private MunitionMax munitionMax = new MunitionMax();

    // Weapons
    public static List<Weapon> weaponList = new ArrayList<>();
    private Mitrailleuse mitrailleuse = new Mitrailleuse();
    private Pistolet pistolet = new Pistolet();
    private Sniper sniper = new Sniper();

    public int getLevel() { return this.level; }
    public List<Weapon> getWeaponList() { return weaponList; }
    public List<Bonus> getBonusList() { return bonus; }
    public Level getStage()
    {
        if (level % 2 == 0) return this.safeZone;
        else return this.noMensLand;
    }
    public ProceduralMap getProceduralMap() {
        return new ProceduralMap(zone);
    }

    public void setBonus()
    {
        // stocker les bonus
        bonus.add(this.potion);
        bonus.add(this.shield);
        bonus.add(this.munitionMax);
    }
    public void setWeaponList()
    {
        // recuperer les armes et leur niveau de munitions
        weaponList.add(this.pistolet);
        weaponList.add(this.mitrailleuse);
        weaponList.add(this.sniper);
    }
    public void setWeaponList(Pistolet pistolet, Mitrailleuse mitrailleuse, Sniper sniper)
    {
        // on recupere les nouvelles armes instanciee avec un chargeur plein
        this.pistolet = pistolet;
        this.mitrailleuse = mitrailleuse;
        this.sniper = sniper;

        // on enleve les anciennes armes usees avec les munitions en moins
        weaponList.clear();

        // on met les nouvelles armes
        weaponList.add(pistolet);
        weaponList.add(mitrailleuse);
        weaponList.add(sniper);
    }
    public void setLevel(int level) { this.level = level; }


    public Level()
    {
        if (bonus.size() == 0) this.setBonus();

        zone = ZoneType.SAFEZONE;

        // MAP
        // this.proceduralMap = new ProceduralMap(ZoneType.SAFEZONE);
    }
    public Level(int level)
    {
        this.level = level;

        if (weaponList.size() == 0) this.setWeaponList();

        zone = ZoneType.NOMENSLAND;

        // MAP
        // this.proceduralMap = new ProceduralMap(ZoneType.NOMENSLAND);
    }
}
