package de.deda.coinsapi.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;

import de.deda.coinsapi.CoinsAPI;

public class MySQL {

	public static String host;
	public static String port;
	public static String database;
	public static String username;
	public static String password;
	public static Connection con;
	
	public static void connect() {
		host = CoinsAPI.getPlugin().getConfig().getString("MySQL.host");
		port = CoinsAPI.getPlugin().getConfig().getString("MySQL.port");
		database = CoinsAPI.getPlugin().getConfig().getString("MySQL.database");
		username = CoinsAPI.getPlugin().getConfig().getString("MySQL.username");
		password = CoinsAPI.getPlugin().getConfig().getString("MySQL.password");
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://"+ host +":"+ port +"/"+ database + "?autoReconnect=true", username, password);
			Server server = CoinsAPI.getPlugin().getServer();
			ConsoleCommandSender console = server.getConsoleSender();
			console.sendMessage(ChatColor.GRAY+"["+ChatColor.LIGHT_PURPLE+"CoinsAPI"+ChatColor.GRAY+"]"+ChatColor.WHITE+" MySQL "+ChatColor.GREEN+"connected");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void disconnect() {
		if(isConnected()) {
			try {
				con.close();
				System.out.println("[CoinsAPI] MySQL disconnected");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean isConnected() {
		return (con == null ? false : true);
	}
	
	public static Connection getConnection() {
		return con;
	}
}
