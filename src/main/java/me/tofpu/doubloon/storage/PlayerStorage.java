package me.tofpu.doubloon.storage;

import java.util.UUID;

public class PlayerStorage {
    private UUID uuid;
    private int doubloon = 0;
    
    PlayerStorage() {
    }
    
    PlayerStorage(final UUID uuid) {
        this.uuid = uuid;
    }
    
    public void setDoubloon(final int doubloon) {
        this.doubloon = doubloon;
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
    public int getDoubloon() {
        return this.doubloon;
    }
}
