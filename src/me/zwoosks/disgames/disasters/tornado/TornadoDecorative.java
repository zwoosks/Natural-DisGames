package me.zwoosks.disgames.disasters.tornado;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class TornadoDecorative {

	public static void spawnTornado(final JavaPlugin plugin, final Location location, final Material material,
			final byte data, final Vector direction, final double speed, final int amount_of_blocks, final long time,
			final boolean spew, final boolean explode) {

		class VortexBlock {

			private Entity entity;

			public boolean removable = true;

			private float ticker_vertical = 0.0f;
			private float ticker_horisontal = (float) (Math.random() * 2 * Math.PI);

			@SuppressWarnings("deprecation")
			public VortexBlock(Location l, Material m, byte d) {

				if (l.getBlock().getType() != Material.AIR) {

					Block b = l.getBlock();
					entity = l.getWorld().spawnFallingBlock(l, b.getType(), b.getData());

					if (b.getType() != Material.WATER)
						b.setType(Material.AIR);

					removable = !spew;
				} else {
					entity = l.getWorld().spawnFallingBlock(l, m, d);
					removable = !explode;
				}

				addMetadata();
			}

			public VortexBlock(Entity e) {
				entity = e;
				removable = false;
				addMetadata();
			}

			private void addMetadata() {
				entity.setMetadata("vortex", new FixedMetadataValue(plugin, "protected"));
			}

			public void remove() {
				if (removable) {
					entity.remove();
				}
				entity.removeMetadata("vortex", plugin);
			}

			@SuppressWarnings("deprecation")
			public HashSet<VortexBlock> tick() {

				double radius = Math.sin(verticalTicker()) * 2;
				float horisontal = horisontalTicker();

				Vector v = new Vector(radius * Math.cos(horisontal), 0.5D, radius * Math.sin(horisontal));

				HashSet<VortexBlock> new_blocks = new HashSet<VortexBlock>();

				// Bloques
				Block b = entity.getLocation().add(v.clone().normalize()).getBlock();
				if (b.getType() != Material.AIR) {
					new_blocks.add(new VortexBlock(b.getLocation(), b.getType(), b.getData()));
				}

				// Otras entidades
				List<Entity> entities = entity.getNearbyEntities(1.0D, 1.0D, 1.0D);
				for (Entity e : entities) {
					if (!e.hasMetadata("vortex")) {
						new_blocks.add(new VortexBlock(e));
					}
				}

				setVelocity(v);

				return new_blocks;
			}

			private void setVelocity(Vector v) {
				entity.setVelocity(v);
			}

			private float verticalTicker() {
				if (ticker_vertical < 1.0f) {
					ticker_vertical += 0.05f;
				}
				return ticker_vertical;
			}

			private float horisontalTicker() {
				// ticker_horisontal = (float) ((ticker_horisontal + 0.8f) % 2*Math.PI);
				return (ticker_horisontal += 0.8f);
			}
		}

		// Modificando dirección a partir del argumento speed.
		if (direction != null) {
			direction.normalize().multiply(speed);
		}

		// Cada bloque creado almacenado aquí para quitar
		// la metadata.
		final HashSet<VortexBlock> clear = new HashSet<VortexBlock>();

		final int id = new BukkitRunnable() {

			private ArrayDeque<VortexBlock> blocks = new ArrayDeque<VortexBlock>();

			public void run() {

				if (direction != null) {
					location.add(direction);
				}

				// Spawnea 10 bloques a cada vuelta.
				for (int i = 0; i < 10; i++) {
					checkListSize();
					VortexBlock vb = new VortexBlock(location, material, data);
					blocks.add(vb);
					clear.add(vb);
				}

				// Girar bloques y llevarse los que estén en el camino.
				ArrayDeque<VortexBlock> que = new ArrayDeque<VortexBlock>();

				for (VortexBlock vb : blocks) {
					HashSet<VortexBlock> new_blocks = vb.tick();
					for (VortexBlock temp : new_blocks) {
						que.add(temp);
					}
				}

				// Añade nuevos bloques
				for (VortexBlock vb : que) {
					checkListSize();
					blocks.add(vb);
					clear.add(vb);
				}
			}

			// Quitar el bloque más viejo si se llega al límite en la lista.
			private void checkListSize() {
				while (blocks.size() >= amount_of_blocks) {
					VortexBlock vb = blocks.getFirst();
					vb.remove();
					blocks.remove(vb);
					clear.remove(vb);
				}
			}
		}.runTaskTimer(plugin, 5L, 5L).getTaskId();

		// Para el tornado después del tiempo indicado.
		new BukkitRunnable() {
			public void run() {
				for (VortexBlock vb : clear) {
					vb.remove();
				}
				plugin.getServer().getScheduler().cancelTask(id);
			}
		}.runTaskLater(plugin, time);
	}

}