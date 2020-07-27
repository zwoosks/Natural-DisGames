package me.zwoosks.disgames;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.util.Vector;

public class EventManager implements Listener {
	
	@SuppressWarnings("unused")
	private Main plugin;

	public EventManager(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		if (e.getEntity() instanceof Arrow && e.getEntity().getShooter() instanceof Player) {
			Player player = (Player) e.getEntity().getShooter();
			@SuppressWarnings("unused")
			Location l = player.getLocation();
		}
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e) {
		for (Block b : e.blockList()) {
			float x = -2.0F + (float) (Math.random() * 4.0D + 1.0D);
			float y = -3.0F + (float) (Math.random() * 6.0D + 1.0D);
			float z = -2.0F + (float) (Math.random() * 4.0D + 1.0D);
			@SuppressWarnings("deprecation")
			org.bukkit.entity.FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType(),
					b.getData());
			((Entity) fb).setVelocity(new Vector(x, y, z));
		}

	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		e.getPlayer().sendMessage(Utils.chat("&achat debug"));
	}

}