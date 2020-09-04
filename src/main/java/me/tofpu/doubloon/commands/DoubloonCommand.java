package me.tofpu.doubloon.commands;

import me.tofpu.doubloon.Doubloon;
import me.tofpu.doubloon.DoubloonManager;
import me.tofpu.doubloon.storage.PlayerStorage;
import me.tofpu.doubloon.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

import static me.tofpu.doubloon.Doubloon.coloredChat;

public class DoubloonCommand implements CommandExecutor {
    private final String[] hasPermissions = {"doubloon.add", "doubloon.deduct", "doubloon.reset", "doubloon.set"};
    private final Doubloon doubloon;
    
    public DoubloonCommand(final Doubloon doubloon) {
        this.doubloon = doubloon;
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final boolean isConsole = !(sender instanceof Player);
        Player player = null;
        if (!isConsole) {
            player = (Player) sender;
        }
        final StorageManager storageManager = this.doubloon.getStorageManager();
        final DoubloonManager doubloonManager = this.doubloon.getDoubloonManager();
        
        final boolean hasArgs0 = args.length < 1;
        if (hasArgs0) {
            if (player != null && !player.hasPermission(Arrays.toString(this.hasPermissions))) {
                player.sendMessage(coloredChat("&c1You do not have permission to execute this command."));
                return true;
            }
            final String usage = String.format("&6Commands: &e/doubloons &6<required> [optional] \n &e/balance &6[player] \n &e/doubloon add &6<player> <amount> &8&m-&r&e %s\n &e/doubloon deduct &6<player> <amount> &8&m-&r&e %s\n &e/doubloon set &6<player> <amount> &8&m-&r&e %s \n &e/doubloon reset &6<player> &8&m-&r&e %s", this.hasPermissions[0], this.hasPermissions[1], this.hasPermissions[3], this.hasPermissions[2]);
            sendMessage(player, sender, coloredChat(usage), isConsole);
            return true;
        }
        final boolean canAdd = args.length == 3;
        final boolean canReset = args.length == 2;
        
        if (args[0].equalsIgnoreCase("add")) {
            if (!isConsole && !hasPermission(player, this.hasPermissions[0])) {
                player.sendMessage(coloredChat("&cYou do not have permission to execute this command."));
                return true;
            }
            if (!canAdd) {
                final String usage = "&eUsage: /doubloon add &6<player> <amount>";
                sendMessage(player, sender, coloredChat(usage), isConsole);
                return true;
            }
            final Player target = Bukkit.getPlayer(args[1]);
            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
            
            if (target == null && !offlinePlayer.hasPlayedBefore()) {
                final String error = "&cI cannot find this player.";
                sendMessage(player, sender, coloredChat(error), isConsole);
                return true;
            }
            final boolean isOn = target != null && target.isOnline();
            
            final int amount;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (final NumberFormatException e) {
                sendMessage(player, sender, coloredChat("&cDo not include characters."), isConsole);
                return true;
            }
            doubloonManager.add(isOn ? target.getUniqueId() : offlinePlayer.getUniqueId(), amount);
            
            sendMessage(player, sender, coloredChat(String.format("&eYou have successfully given &6%d &edoubloons to &6%s&e!", amount, args[1])), isConsole);
            return true;
        } else if (args[0].equalsIgnoreCase("deduct")) {
            if (!isConsole && !hasPermission(player, this.hasPermissions[1])) {
                player.sendMessage(coloredChat("&cYou do not have permission to execute this command."));
                return true;
            }
            if (!canAdd) {
                final String usage = "&eUsage: /doubloon deduct &6<player> <amount>";
                sendMessage(player, sender, coloredChat(usage), isConsole);
                return true;
            }
            final Player target = Bukkit.getPlayer(args[1]);
            
            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
            if (target == null && !offlinePlayer.hasPlayedBefore()) {
                final String error = "&cI cannot find this player.";
                sendMessage(player, sender, coloredChat(error), isConsole);
                return true;
            }
            final boolean isOn = target != null && target.isOnline();
            
            final int amount;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (final NumberFormatException e) {
                sendMessage(player, sender, coloredChat("&cDo not include characters."), isConsole);
                return true;
            }
            final boolean success = doubloonManager.deduct(isOn ? target.getUniqueId() : offlinePlayer.getUniqueId(), amount);
            
            if (success) {
                sendMessage(player, sender, coloredChat(String.format("&eYou have successfully deducted &6%d &edoubloons from &6%s&e!", amount, args[1])), isConsole);
            } else {
                sendMessage(player, sender, coloredChat(String.format("&6%s &edoes not have enough doubloons.", args[1])), isConsole);
            }
            return true;
        } else if (args[0].equalsIgnoreCase("set")) {
            if (!isConsole && !hasPermission(player, this.hasPermissions[3])) {
                player.sendMessage(coloredChat("&eYou do not have permission to execute this command."));
                return true;
            }
            if (!canAdd) {
                final String usage = "&eUsage: /doubloon set &6<player> <amount>";
                sendMessage(player, sender, coloredChat(usage), isConsole);
                return true;
            }
            final Player target = Bukkit.getPlayer(args[1]);
            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
            
            if (target == null) {
                if (!offlinePlayer.hasPlayedBefore()) {
                    final String error = "&cI cannot find this player.";
                    sendMessage(player, sender, coloredChat(error), isConsole);
                    return true;
                }
            }
            
            final boolean isOn = target != null && target.isOnline();
            final PlayerStorage storage = isOn ? storageManager.getStorage(target.getUniqueId()) : storageManager.getStorage(offlinePlayer.getUniqueId());
            
            final int amount;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (final NumberFormatException e) {
                sendMessage(player, sender, coloredChat("&cDo not include characters."), isConsole);
                return true;
            }
            storage.setDoubloon(amount);
            
            sendMessage(player, sender, coloredChat(String.format("&eYou have successfully set &6%s &eto &6%d &edoubloons!", args[1], amount)), isConsole);
            return true;
        } else if (args[0].equalsIgnoreCase("reset")) {
            if (!isConsole && !hasPermission(player, this.hasPermissions[2])) {
                player.sendMessage(coloredChat("&eYou do not have permission to execute this command."));
                return true;
            }
            if (!canReset) {
                final String usage = "&eUsage: /doubloon reset &6<player>";
                sendMessage(player, sender, coloredChat(usage), isConsole);
                return true;
            }
            final Player target = Bukkit.getPlayer(args[1]);
            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
            
            if (target == null) {
                if (!offlinePlayer.hasPlayedBefore()) {
                    final String error = "&cI cannot find this player.";
                    sendMessage(player, sender, coloredChat(error), isConsole);
                    return true;
                }
            }
            final boolean isOn = target != null && target.isOnline();
            final int amount = isOn ? storageManager.getStorage(target.getUniqueId()).getDoubloon() : storageManager.getStorage(offlinePlayer.getUniqueId()).getDoubloon();
            
            if (isOn) {
                doubloonManager.deduct(target.getUniqueId(), amount);
            } else {
                doubloonManager.deduct(offlinePlayer.getUniqueId(), amount);
            }
            sendMessage(player, sender, coloredChat(String.format("&eYou have successfully reset &6%s &edoubloons!", args[1])), isConsole);
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
    
    public boolean hasPermission(final Player player, final String node) {
        if (player == null) {
            return true;
        }
        if (player.isOp()) {
            return true;
        }
        return player.hasPermission(node);
    }
}
