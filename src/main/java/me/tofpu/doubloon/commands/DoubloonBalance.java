package me.tofpu.doubloon.commands;

import me.tofpu.doubloon.Doubloon;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

import static me.tofpu.doubloon.Doubloon.coloredChat;

public class DoubloonBalance implements CommandExecutor {
    private final Doubloon doubloon;
    
    public DoubloonBalance(final Doubloon doubloon) {
        this.doubloon = doubloon;
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final boolean isConsole = !(sender instanceof Player);
        Player player = null;
        if (!isConsole) {
            player = (Player) sender;
        }
        
        final boolean hasArgs0 = args.length == 1;
        if (isConsole && !hasArgs0) {
            sender.sendMessage(coloredChat("This is a player only command."));
            return true;
        }
        if (args.length < 1) {
            final UUID uuid = player.getUniqueId();
            player.sendMessage(coloredChat(String.format("&eYou have &6%d &edoubloons.", this.doubloon.getStorageManager().getStorage(uuid).getDoubloon())));
            return true;
        }
        if (hasArgs0) {
            final Player target = Bukkit.getPlayer(args[0]);
            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if (target == null && !offlinePlayer.hasPlayedBefore()) {
                final String error = "&cI cannot find this player.";
                sendMessage(player, sender, coloredChat(error), isConsole);
                return true;
            }
            final boolean isOn = target != null && target.isOnline();

//            final int doubloons;
//            if (isOn) {
//                doubloons = this.doubloon.getStorageManager().getStorage(target.getUniqueId()).getDoubloon();
//                sendMessage(player, sender, coloredChat(String.format("&e%s has &6%d &edoubloons.", target.getName(), doubloons)), isConsole);
//            } else {
//                doubloons = this.doubloon.getStorageManager().getStorage(offlinePlayer.getUniqueId()).getDoubloon();
//                sendMessage(player, sender, coloredChat(String.format("&e%s has &6%d &edoubloons.", offlinePlayer.getName(), doubloons)), isConsole);
//                doubloons = this.doubloon.getStorageManager().getStorage(target.getUniqueId()).getDoubloon();
//            }
            final int doubloons = this.doubloon.getStorageManager().getStorage(isOn ? target.getUniqueId() : offlinePlayer.getUniqueId()).getDoubloon();
            sendMessage(player, sender, coloredChat(String.format("&6%s &ehas &6%d &edoubloons.", isOn ? target.getName() : offlinePlayer.getName(), doubloons)), isConsole);
            return true;
        }
        return false;
    }
    
    public void sendMessage(final Player player, final CommandSender sender, final String message, final boolean isOp) {
        if (isOp) {
            sender.sendMessage(message);
            return;
        }
        player.sendMessage(message);
    }
}
