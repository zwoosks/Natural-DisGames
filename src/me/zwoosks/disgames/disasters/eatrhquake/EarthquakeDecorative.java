package me.zwoosks.disgames.disasters.eatrhquake;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import com.google.common.collect.Lists;

import me.zwoosks.disgames.Main;

public class EarthquakeDecorative {

	private Main plugin;
	private final Location center;
	private final int radius;
	private final long delay;

	public EarthquakeDecorative(Main plugin, Location center) {
		this.plugin = plugin;
		this.center = center;
		
		int minRadius = plugin.getConfig().getInt("disasters.earthquake.minRadius");
		int maxRadius = plugin.getConfig().getInt("disasters.earthquake.maxRadius");

		Random r = new Random();
		this.radius = r.nextInt((maxRadius - minRadius) + 1) + minRadius;
		this.delay = plugin.getConfig().getLong("disasters.earthquake.delay");
	}

	public void spawn() {
		BukkitScheduler scheduler = plugin.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
			int counter = 0;
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (counter >= radius)
					return;
				counter++;
				List<Block> blocks = getCircle(center, counter);
				for (Block b : blocks) {
					Material m = b.getType();
					byte data = b.getData();
					b.setType(Material.AIR);
					FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), m, data);
					fb.setVelocity(new Vector(0.0D, 0.1D, 0.0D));
				}
			}
		}, 0L, delay);
	}

	@SuppressWarnings("deprecation")
	private List<Block> getCircle(Location center, int r) {
		List<Block> blocks = Lists.newArrayList();
		double x;
		for (x = -r; x <= r; x++) {
			double z;
			for (z = -r; z <= r; z++) {
				if ((int) center.clone().add(x, 0.0D, z).distance(center) == r)
					blocks.add(center.clone().add(x, 0.0D, z).getBlock());
			}
		}
		List<Block> toReturn = new ArrayList<Block>();
		for(Block b : blocks) {
			Location l = b.getLocation();
			if (net.minecraft.server.v1_8_R3.Block.getById(b.getTypeId()).c()) {
				while(net.minecraft.server.v1_8_R3.Block.getById(l.getBlock().getTypeId()).c()) {
					l.setY(l.getY() + 1);
				}
				l.setY(l.getY() - 1);
				toReturn.add(l.getBlock());
			} else {
				while(!net.minecraft.server.v1_8_R3.Block.getById(l.getBlock().getTypeId()).c()) {
					l.setY(l.getY() - 1);
				}
				toReturn.add(l.getBlock());
			}
		}
		return toReturn;
	}

}