package com.comandante.creeper.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerMetadata implements Serializable {

    String playerName;
    String password;
    String playerId;
    int health;
    int stamina;
    int strength;
    int dexterity;
    String[] inventory;

    public PlayerMetadata(String playerName, String password, String playerId) {
        this.playerName = playerName;
        this.password = password;
        this.playerId = playerId;
    }

    public String[] getInventory() {
            return inventory;
    }

    public void setInventory(String[] inventory) {
        this.inventory = inventory;
    }

    public void addInventoryEntityId(String newEntityId) {
            if (inventory == null) {
                inventory = new String[0];
            }
            String[] result = Arrays.copyOf(inventory, inventory.length + 1);
            result[inventory.length] = newEntityId;
            this.inventory = result;
    }

    public void removeInventoryEntityId(String itemId) {
        List<String> itemsIdKeep = new ArrayList<String>(Arrays.asList(inventory));
        itemsIdKeep.remove(itemId);
        String[] newItems = new String[itemsIdKeep.size()];
        int i = 0;
        for (String id: itemsIdKeep) {
            newItems[i] = id;
            i++;
        }
        this.inventory = newItems;
    }

    public String getPassword() {
        return password;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getHealth() {
        return health;
    }

    public int getStamina() {
        return stamina;
    }

    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

}
