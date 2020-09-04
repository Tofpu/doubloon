package me.tofpu.doubloon.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.tofpu.doubloon.Doubloon;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StorageManager {
    private final Doubloon doubloon;
    private ArrayList<PlayerStorage> cache = new ArrayList<>();
    
    public StorageManager(final Doubloon doubloon) {
        this.doubloon = doubloon;
    }
    
    /**
     * If you want to get player storage (doubloon), you would have to use this.
     *
     * @param uuid player uuid (player#getUniqueId())
     * @return player storage
     */
    
    public PlayerStorage getStorage(final UUID uuid) {
        for (int i = 0; i < getCache().size(); i++) {
            final PlayerStorage storage = this.cache.get(i);
            if (storage.getUuid().equals(uuid)) {
                return storage;
            }
        }
        final PlayerStorage storage = new PlayerStorage(uuid);
        this.cache.add(storage);
        return storage;
    }
    
    public void save() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        final File folder = new File(this.doubloon.getDataFolder().getPath());
        if (!folder.exists()) {
            folder.mkdir();
        }
        
        final File storage = new File(this.doubloon.getDataFolder(), "storage.json");
        if (!storage.exists()) {
            try {
                storage.createNewFile();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        
        final FileWriter writer;
        try {
            writer = new FileWriter(storage);
            writer.write(gson.toJson(this.cache));
            writer.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public void load() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final File storage = new File(this.doubloon.getDataFolder(), "storage.json");
        
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(storage));
            final Type listType = new TypeToken<List<PlayerStorage>>() {
            }.getType();
            final ArrayList<PlayerStorage> temp = gson.fromJson(reader, listType);
            if (temp != null) {
                this.cache = temp;
            }
            reader.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<PlayerStorage> getCache() {
        return this.cache;
    }
}
