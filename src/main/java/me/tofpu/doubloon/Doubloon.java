package me.tofpu.doubloon;

import me.tofpu.doubloon.api.PlaceholderExpansion;
import me.tofpu.doubloon.commands.DoubloonBalance;
import me.tofpu.doubloon.commands.DoubloonCommand;
import me.tofpu.doubloon.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Doubloon extends JavaPlugin implements Listener {
    
    private StorageManager storageManager;
    private DoubloonManager doubloonManager;
    
    @Override
    public void onEnable() {
        // Plugin startup logic
        setStorageManager(new StorageManager(this));
        getStorageManager().load();
        
        this.doubloonManager = new DoubloonManager(this);
        
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderExpansion(this).register();
        }
        
        Bukkit.getPluginCommand("doubloons").setExecutor(new DoubloonBalance(this));
        Bukkit.getPluginCommand("doubloon").setExecutor(new DoubloonCommand(this));
    }
    
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.storageManager.save();
    }
    
    public void setStorageManager(final StorageManager storageManager) {
        this.storageManager = storageManager;
    }
    
    public StorageManager getStorageManager() {
        return this.storageManager;
    }
    
    public DoubloonManager getDoubloonManager() {
        return this.doubloonManager;
    }
    
    public static String coloredChat(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
