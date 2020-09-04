package me.tofpu.doubloon.api;

import me.tofpu.doubloon.Doubloon;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderExpansion extends me.clip.placeholderapi.expansion.PlaceholderExpansion {
    private final Doubloon doubloon;
    
    public PlaceholderExpansion(final Doubloon doubloon) {
        this.doubloon = doubloon;
    }
    
    @Override
    public @NotNull String getIdentifier() {
        return "doubloon";
    }
    
    @Override
    public boolean persist() {
        return true;
    }
    
    @Override
    public boolean canRegister() {
        return true;
    }
    
    @Override
    public @NotNull String getAuthor() {
        return "Tofpu";
    }
    
    @Override
    public @NotNull String getVersion() {
        return this.doubloon.getDescription().getVersion();
    }
    
    @Override
    public String onPlaceholderRequest(final Player player, @NotNull final String params) {
        if (player == null) {
            return "";
        }
        return this.doubloon.getStorageManager().getStorage(player.getUniqueId()).getDoubloon() + "";
    }
}
