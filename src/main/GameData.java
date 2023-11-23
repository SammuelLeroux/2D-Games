package main;

import java.io.*;
import java.lang.reflect.Field;

import entities.Player;
import level.*;

public class GameData implements Serializable
{
    Player player;
    Level stage;

    public GameData(Player player, Level stage)
    {
        this.player = player;
        this.stage = stage;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject(); // Écrit les champs non-transitoires
    
        // Utilise la réflexion pour accéder aux champs annotés de Player
        Class<?> playerClass = player.getClass();
        Field[] fields = playerClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ToSave.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(player);
    
                    // Vérifie si le champ implémente Serializable avant de le sérialiser
                    if (value instanceof Serializable) {
                        out.writeObject(value);
                    } else {
                        // Gérer le cas où le champ n'est pas sérialisable
                        System.err.println("Field " + field.getName() + " is not serializable.");
                    }
    
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // Lit les champs non-transitoires
    
        // Utilise la réflexion pour restaurer les champs annotés de Player
        Class<?> playerClass = Player.class; // Correction ici
        Field[] fields = playerClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ToSave.class)) {
                field.setAccessible(true);
                try {
                    Object value = in.readObject();
                    field.set(player, value);
                } catch (IllegalAccessException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}
