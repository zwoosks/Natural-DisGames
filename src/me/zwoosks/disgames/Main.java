package me.zwoosks.disgames;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		registerCommands();
		registerEvents();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private void registerCommands() {
		CommandExecutor ce = new CommandManager(this);
		getCommand("disaster").setExecutor(ce);
	}
	
	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new EventManager(this), this);
	}
	
}