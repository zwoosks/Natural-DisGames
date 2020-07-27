package me.zwoosks.disgames;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.zwoosks.disgames.disasters.eatrhquake.EarthquakeDecorative;
import me.zwoosks.disgames.disasters.lightning.Lightning;
import me.zwoosks.disgames.disasters.tornado.TornadoDecorative;
import me.zwoosks.disgames.disasters.tornado.TornadoExplosive;

public class CommandManager implements CommandExecutor {

	private Main plugin;

	public CommandManager(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("disaster") && sender.hasPermission("disgames.spawndisaster")
				&& sender instanceof Player && args.length >= 1) {
			Player player = (Player) sender;
			Location location = player.getLocation();
			if (args[0].equalsIgnoreCase("tornado")) {
				TornadoExplosive tornado = new TornadoExplosive(plugin, location);
				tornado.spawn();
			} else if (args[0].equalsIgnoreCase("earthquake")) {
				EarthquakeDecorative earthquake = new EarthquakeDecorative(plugin, location);
				earthquake.spawn();
			} else if (args[0].equalsIgnoreCase("lightning")) {
				Lightning.spawn(plugin, location);
			} else if (args[0].equalsIgnoreCase("tornadov2")) {
				TornadoDecorative.spawnTornado(plugin, location, Material.GRASS, (byte) 0, new Vector(100, 10, 0), 0.3, 200, (long) 30*20, false, false);
			}
		}
		return true;
	}

}
