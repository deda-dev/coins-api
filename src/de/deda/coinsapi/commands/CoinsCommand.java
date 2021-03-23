package de.deda.coinsapi.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.deda.coinsapi.mysql.MySQLCoins;

public class CoinsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			if(args.length != 3) {
				sender.sendMessage("§cUse /coins <set/add/remove> <player> <amount>");
				return false;
			}
			
			if(!sender.hasPermission("CoinsAPI.coins")) {
				sender.sendMessage("§cNo permissions.");
				return false;
			}
			
			if(Bukkit.getPlayer(args[1]) == null) {
				sender.sendMessage("§cThe player §6"+args[1]+" §care not online.");
				return false;
			}
			
			if(!isInteger(args[2])) {
				sender.sendMessage("§cUse /coins <set/add/remove> <player> <amount>");
				return false;
			}
			
			Player target = Bukkit.getPlayer(args[1]);
			UUID targetUUID = target.getUniqueId();
			int amount = Integer.parseInt(args[2]);
			
			switch(args[0]) {
			case "set":
				MySQLCoins.setCoins(targetUUID, amount);
				sender.sendMessage("§aYou set §6"+amount+" §acoins to player §6"+target.getDisplayName());
				return true;
				
			case "add":
				MySQLCoins.addCoins(targetUUID, amount);
				sender.sendMessage("§aYou add §6"+amount+" §acoins to player §6"+target.getDisplayName());
				return true;
				
			case "remove":
				MySQLCoins.removeCoins(targetUUID, amount);
				sender.sendMessage("§aYou removed §6"+amount+" §acoins from player §6"+target.getDisplayName());
				return true;
			
			default:
				sender.sendMessage("§cUse /coins <set/add/remove> <player> <amount>");
				return false;
			}
		}
		
		Player player = (Player) sender;
		String coinsPlayer = getPlayerCoins(player);
		
		if(args.length == 0) {
			player.sendMessage("§7Your coins: §6"+coinsPlayer);
			return true;
		}
		
		if(args.length == 1) {
			if("help".equalsIgnoreCase(args[0])) {
				if(!player.hasPermission("CoinsAPI.coins")) {
					player.sendMessage("§cUse /coins <player> §7to see your or other player coins.");
					return false;
				}	
				player.sendMessage("§cUse /coins <player> §7to see your or other player coins.");
				player.sendMessage("§cUse /coins <set/add/remove> <player> <amount> §7to edit player coins.");
				return false;
			}
			
			if(Bukkit.getPlayer(args[0]) == null) {
				player.sendMessage("§cThe player §6"+args[0]+" §care not online.");
				return false;
			}
			Player target = Bukkit.getPlayer(args[0]);
			String coinsTarget = getPlayerCoins(target);
		
			player.sendMessage("§7"+target.getDisplayName()+" coins: §6"+coinsTarget);
			return true;
		}
		
		if(args.length != 3) {
			player.sendMessage("§cUse /coins <set/add/remove> <player> <amount>");
			return false;
		}
		
		if(!player.hasPermission("CoinsAPI.coins")) {
			player.sendMessage("§cNo permissions.");
			return false;
		}
		
		if(Bukkit.getPlayer(args[1]) == null) {
			player.sendMessage("§cThe player §6"+args[1]+" §care not online.");
			return false;
		}
		
		if(!isInteger(args[2])) {
			player.sendMessage("§cUse /coins <set/add/remove> <player> <amount>");
			return false;
		}
		
		Player target = Bukkit.getPlayer(args[1]);
		UUID targetUUID = target.getUniqueId();
		int amount = Integer.parseInt(args[2]);
		
		switch(args[0]) {
		case "set":
			MySQLCoins.setCoins(targetUUID, amount);
			player.sendMessage("§aYou set §6"+amount+" §acoins to player §6"+target.getDisplayName());
			return true;
			
		case "add":
			MySQLCoins.addCoins(targetUUID, amount);
			player.sendMessage("§aYou add §6"+amount+" §acoins to player §6"+target.getDisplayName());
			return true;
			
		case "remove":
			MySQLCoins.removeCoins(targetUUID, amount);
			player.sendMessage("§aYou removed §6"+amount+" §acoins from player §6"+target.getDisplayName());
			return true;
		
		default:
			player.sendMessage("§cUse /coins <set/add/remove> <player> <amount>");
			return false;
		}
	}

	private boolean isInteger(String string) {
		try {
			Integer.parseInt(string);
			Double.parseDouble(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static String getPlayerCoins(Player player) {
		UUID uuid = player.getUniqueId();
		
		if(!MySQLCoins.isUserExists(uuid)) {
			MySQLCoins.setPlayerIn(uuid, 0);
			return "0";
		}
		int coins = MySQLCoins.getCoins(uuid);
		
		String coinString = ""+coins;
		char[] coinsArray = coinString.toCharArray();
	
		if(coins >= 100000000) {
			String coinString1 = ""+coinsArray[0]+coinsArray[1]+coinsArray[2];
			String coinString2 = ""+coinsArray[3]+coinsArray[4]+coinsArray[5];
			String coinString3 = ""+coinsArray[6]+coinsArray[7]+coinsArray[8];
			String finishedCoins = coinString1+"."+coinString2+"."+coinString3;
			
			return finishedCoins;
		}
		if(coins >= 10000000) {
			String coinString1 = ""+coinsArray[0]+coinsArray[1];
			String coinString2 = ""+coinsArray[2]+coinsArray[3]+coinsArray[4];
			String coinString3 = ""+coinsArray[5]+coinsArray[6]+coinsArray[7];
			String finishedCoins = coinString1+"."+coinString2+"."+coinString3;
			
			return finishedCoins;
		}
		if(coins >= 1000000) {
			String coinString1 = ""+coinsArray[0];
			String coinString2 = ""+coinsArray[1]+coinsArray[2]+coinsArray[3];
			String coinString3 = ""+coinsArray[4]+coinsArray[5]+coinsArray[6];
			String finishedCoins = coinString1+"."+coinString2+"."+coinString3;
			
			return finishedCoins;
		}
		if(coins >= 100000) {
			String coinString1 = ""+coinsArray[0]+coinsArray[1]+coinsArray[2];
			String coinString2 = ""+coinsArray[3]+coinsArray[4]+coinsArray[5];
			String finishedCoins = coinString1+"."+coinString2;
			
			return finishedCoins;
		}
		if(coins >= 10000) {
			String coinString1 = ""+coinsArray[0]+coinsArray[1];
			String coinString2 = ""+coinsArray[2]+coinsArray[3]+coinsArray[4];
			String finishedCoins = coinString1+"."+coinString2;
			
			return finishedCoins;
		}
		if(coins >= 1000) {
			String coinString1 = ""+coinsArray[0];
			String coinString2 = ""+coinsArray[1]+coinsArray[2]+coinsArray[3];
			String finishedCoins = coinString1+"."+coinString2;
			
			return finishedCoins;
		}
		return ""+coins;
	}
}
