package de.deda.coinsapi;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import de.deda.coinsapi.commands.CoinsCommand;
import de.deda.coinsapi.mysql.MySQL;
import de.deda.coinsapi.mysql.MySQLCoins;

public class CoinsAPI extends JavaPlugin implements Listener {
	
	private static CoinsAPI plugin;
	
	public static void setPlayerIn(Player player, int amount) {
		MySQLCoins.setPlayerIn(player.getUniqueId(), amount);
	}
	
	public static boolean isPlayerExists(Player player) {
		return MySQLCoins.isUserExists(player.getUniqueId());
	}
	
	public static void setPlayerCoins(Player player, int amount) {
		MySQLCoins.setCoins(player.getUniqueId(), amount);
	}
	
	public static void addPlayerCoins(Player player, int amount) {
		MySQLCoins.addCoins(player.getUniqueId(), amount);
	}
	
	public static void removePlayerCoins(Player player, int amount) {
		MySQLCoins.removeCoins(player.getUniqueId(), amount);
	}
	
	public static int getPlayerCoins(Player player) {
		return MySQLCoins.getCoins(player.getUniqueId());
	}
	
	public static void deletePlayerFromDatabase(Player player) {
		MySQLCoins.delete(player.getUniqueId());
	}
	
	@Override
	public void onEnable() {
		plugin = this;
		Server server = getServer();
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		ConsoleCommandSender console = server.getConsoleSender();
        console.sendMessage(ChatColor.GRAY+"["+ChatColor.LIGHT_PURPLE+"CoinsAPI"+ChatColor.GRAY+"]"+ChatColor.GREEN+" enabled");
		
		//Database
		if(getPlugin().getConfig().getBoolean("MySQL.enabled")) {
			MySQL.connect();
			try {
				PreparedStatement coins = MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS CoinsSystem (UUID VARCHAR(36),Coins INT(20))");
				coins.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//Register Listeners
		Bukkit.getPluginManager().registerEvents(this, this);
		
		//Register Command
		getCommand("coins").setExecutor(new CoinsCommand());
	}
	
	@Override
	public void onDisable() {
		MySQL.disconnect();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if(getPlugin().getConfig().getBoolean("MySQL.enabled"))
			MySQLCoins.setPlayerIn(event.getPlayer().getUniqueId(), 0);
	}
	
	public static CoinsAPI getPlugin() {
		return plugin;
	}
}
