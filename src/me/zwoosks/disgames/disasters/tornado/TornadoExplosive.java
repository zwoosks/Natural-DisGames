package me.zwoosks.disgames.disasters.tornado;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitScheduler;

import me.zwoosks.disgames.Main;

public class TornadoExplosive {

	private Main plugin;
	private FileConfiguration config;

	private Location location;
	private final String direction;
	private boolean directionTrend; // false Left, true right
	private final int min;
	private final int max;
	private final int exPerColumn;
	private final int separationExplosions;
	private final int columns;
	private final int minSeparation;
	private final int maxSeparation;
	private final int minDeflection;
	private final int maxDeflection;
	private final int percentDirection;
	private final long columnDelay;

	public TornadoExplosive(Main plugin, Location location) {
		
		this.plugin = plugin;
		this.config = plugin.getConfig();
		this.location = location;
		this.directionTrend = config.getBoolean("disasters.tornado.directionTrend");
		this.direction = config.getString("disasters.tornado.direction");
		this.min = config.getInt("disasters.tornado.min");
		this.max = config.getInt("disasters.tornado.max");
		this.exPerColumn = config.getInt("disasters.tornado.exPerColumn");
		this.separationExplosions = config.getInt("disasters.tornado.separationExplosions");
		this.minSeparation = config.getInt("disasters.tornado.minSeparation");
		this.maxSeparation = config.getInt("disasters.tornado.maxSeparation");
		this.minDeflection = config.getInt("disasters.tornado.minDeflection");
		this.maxDeflection = config.getInt("disasters.tornado.maxDeflection");
		this.percentDirection = config.getInt("disasters.tornado.percentDirection");
		this.columnDelay = config.getInt("disasters.tornado.columnDelay");
		
		int minColumns = config.getInt("disasters.tornado.minColumns");
		int maxColumns = config.getInt("disasters.tornado.maxColumns");
		
		Random r = new Random();
		this.columns = r.nextInt((maxColumns - minColumns) + 1) + minColumns;
		
	}

	public void spawn() {
		BukkitScheduler scheduler = plugin.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
			int counter = 0;
			Location lc_local_spawn = location;
			@Override
			public void run() {
				if(counter >= columns) return;
				counter++;
				column(lc_local_spawn);
				Location nova_lc = moveTornado(lc_local_spawn);
				lc_local_spawn.setX(nova_lc.getX());
				lc_local_spawn.setY(nova_lc.getY());
				lc_local_spawn.setZ(nova_lc.getZ());
			}
		}, 0L, columnDelay);
	}

	private Location moveTornado(Location location) {
		Location toReturn = location;
		Random r = new Random();
		int dirP = r.nextInt(101);
		boolean conserv = (percentDirection <= dirP);
		boolean localTrend = directionTrend;
		if (!conserv)
			localTrend = !directionTrend;
		int deflection = r.nextInt((maxDeflection - minDeflection) + 1) + minDeflection;
		int separation = r.nextInt((maxSeparation - minSeparation) + 1) + minSeparation;
		switch (direction.toLowerCase()) {
		case "w":
			toReturn.setX(toReturn.getX() - separation); // X Negativa
			if (localTrend) {
				toReturn.setZ(toReturn.getZ() - deflection);
			} else {
				toReturn.setZ(toReturn.getZ() + deflection);
			}
			break;
		case "e":
			toReturn.setX(toReturn.getX() + separation); // X Positiva
			if (localTrend) {
				toReturn.setZ(toReturn.getZ() + deflection);
			} else {
				toReturn.setZ(toReturn.getZ() - deflection);
			}
			break;
		case "n":
			location.setZ(location.getZ() - separation); // Z Negativa
			if (localTrend) {
				location.setX(location.getX() - deflection);
			} else {
				location.setX(location.getX() + deflection);
			}
			break;
		case "s":
			location.setZ(location.getZ() + separation); // Z Positiva
			if (localTrend) {
				location.setX(location.getX() + deflection);
			} else {
				location.setX(location.getX() - deflection);
			}
			break;
		default:
			break;
		}
		return location;
	}

	private void column(Location lc1) {
		World world = lc1.getWorld();
		Random r = new Random();
		for(int i = 0; i <= exPerColumn; i++) {
			int range = r.nextInt((max - min) + 1) + min;
			world.createExplosion(lc1, range);
			lc1.setY(lc1.getY() + separationExplosions);
		}
		lc1.setY(lc1.getWorld().getHighestBlockYAt(lc1) - 2);
	}

}