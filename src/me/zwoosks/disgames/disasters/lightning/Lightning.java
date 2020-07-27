package me.zwoosks.disgames.disasters.lightning;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;

import api.praya.acidrain.main.AcidRainAPI;
import me.zwoosks.disgames.Main;

public class Lightning {
	
	public static void spawn(Main plugin, Location location) {
		location.getWorld().strikeLightning(location);
		Entity tnt = location.getWorld().spawn(location, TNTPrimed.class);
		((TNTPrimed)tnt).setFuseTicks(0);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
		    @Override
		    public void run() {
		    	((TNTPrimed)tnt).remove();
		    	AcidRainAPI api = AcidRainAPI.getInstance();
		    	api.getGameManagerAPI().getWorldRainManagerAPI().setAcidRain(tnt.getWorld(), 10);
		    }
		}, 20L);
	} 
	
}