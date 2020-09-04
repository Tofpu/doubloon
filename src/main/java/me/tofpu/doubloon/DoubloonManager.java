package me.tofpu.doubloon;

import me.tofpu.doubloon.storage.PlayerStorage;
import me.tofpu.doubloon.storage.StorageManager;

import java.util.UUID;

public class DoubloonManager {
    private final StorageManager manager;
    
    DoubloonManager(final Doubloon doubloon) {
        this.manager = doubloon.getStorageManager();
    }
    
    /**
     * Adds a certain amount of doubloon.
     * You can also get player storage with the player uuid via StorageManager
     *
     * @param storage - you can get storage by
     * @param amount  - how many doubloons you want to give to the player
     * @return boolean - returns true if the transaction succeeds or else, it returns false.
     */
    public boolean deduct(final PlayerStorage storage, final int amount) {
        if (storage == null) {
            return false;
        }
        final int doubloons = storage.getDoubloon();
        
        if (doubloons < amount) {
            return false;
        }
        
        storage.setDoubloon(doubloons - amount);
        return true;
    }
    
    /**
     * Deducts a certain amount of doubloon.
     * You can also get player storage with the player uuid via StorageManager
     *
     * @param uuid   player uuid (player#getUniqueId())
     * @param amount how many doubloons you want to give to the player
     * @return boolean - returns true if the transaction succeeds or else, it returns false.
     */
    
    public boolean deduct(final UUID uuid, final int amount) {
        final PlayerStorage storage = this.manager.getStorage(uuid);
        
        if (storage == null) {
            return false;
        }
        final int doubloons = storage.getDoubloon();
        
        if (doubloons < amount) {
            return false;
        }
        
        storage.setDoubloon(doubloons - amount);
        return true;
    }
    
    /**
     * Adds a certain amount of doubloon.
     * You can also get player storage with the player uuid via StorageManager
     *
     * @param storage you can get storage by
     * @param amount  how many doubloons you want to give to the player
     * @return boolean - returns true if the transaction succeeds or else, it returns false.
     */
    public boolean add(final PlayerStorage storage, final int amount) {
        if (storage == null) {
            return false;
        }
        final int doubloons = storage.getDoubloon();
        
        storage.setDoubloon(doubloons + amount);
        return true;
    }
    
    /**
     * Adds a certain amount of doubloon.
     * You can also get player storage with the player uuid via StorageManager
     *
     * @param uuid   - player uuid (player#getUniqueId())
     * @param amount - how many doubloons you want to give to the player
     * @return boolean - returns true if the transaction succeeds or else, it returns false.
     */
    
    public boolean add(final UUID uuid, final int amount) {
        final PlayerStorage storage = this.manager.getStorage(uuid);
        
        if (storage == null) {
            return false;
        }
        final int doubloons = storage.getDoubloon();
        
        storage.setDoubloon(doubloons + amount);
        return true;
    }
    
}
