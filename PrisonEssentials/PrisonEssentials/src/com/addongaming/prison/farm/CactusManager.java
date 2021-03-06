package com.addongaming.prison.farm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.addongaming.hcessentials.HcEssentials;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class CactusManager implements Listener {
	List<String> cactusFarms = new ArrayList<String>();

	public CactusManager(JavaPlugin jp) {
		initConfig(jp);
		loadConfig(jp);
		jp.getServer().getPluginManager().registerEvents(this, jp);
	}

	@EventHandler
	public void blockBreakEvent(BlockBreakEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
			return;
		ApplicableRegionSet ars = HcEssentials.worldGuard.getRegionManager(
				event.getBlock().getWorld()).getApplicableRegions(
				event.getBlock().getLocation());
		for (Iterator<ProtectedRegion> it = ars.iterator(); it.hasNext();) {
			ProtectedRegion pr = it.next();
			for (String region : cactusFarms)
				if (pr.getId().equalsIgnoreCase(region)) {
					if (event.getBlock().getType() == Material.CACTUS) {
						if (event.getBlock().getRelative(BlockFace.DOWN)
								.getType() == Material.CACTUS)
							return;
					}
					event.setCancelled(true);
					return;
				}
		}
	}

	@EventHandler
	public void blockPlace(BlockPlaceEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
			return;
		ApplicableRegionSet ars = HcEssentials.worldGuard.getRegionManager(
				event.getBlock().getWorld()).getApplicableRegions(
				event.getBlock().getLocation());
		for (Iterator<ProtectedRegion> it = ars.iterator(); it.hasNext();) {
			ProtectedRegion pr = it.next();
			for (String region : cactusFarms) {
				if (pr.getId().equalsIgnoreCase(region)) {
					event.setCancelled(true);
					return;
				}
			}
		}
	}

	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasItem()) {
			ApplicableRegionSet ars = HcEssentials.worldGuard.getRegionManager(
					event.getClickedBlock().getWorld()).getApplicableRegions(
					event.getClickedBlock().getLocation());
			for (Iterator<ProtectedRegion> it = ars.iterator(); it.hasNext();) {
				ProtectedRegion pr = it.next();
				for (String region : cactusFarms) {
					if (pr.getId().equalsIgnoreCase(region)) {
						if (blockPlace(event.getItem().getType(),
								event.getPlayer()))
							event.setCancelled(true);
						return;
					}
				}
			}
		}
	}

	Material[] dangerousItems = { Material.LAVA_BUCKET, Material.LAVA,
			Material.GRAVEL, Material.FLINT_AND_STEEL };

	public boolean blockPlace(Material place, Player player) {
		System.out.println(place.name());
		for (Material mat : dangerousItems)
			if (place == mat) {
				return true;
			}
		return false;
	}

	private void initConfig(JavaPlugin jp) {
		FileConfiguration fc = jp.getConfig();
		fc.addDefault("prison.farms.cactus.name", new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				this.add("bcactusfarm");
			}
		});
		fc.options().copyDefaults(true);
		jp.saveConfig();
	}

	private void loadConfig(JavaPlugin jp) {
		FileConfiguration fc = jp.getConfig();
		cactusFarms = fc.getStringList("prison.farms.cactus.name");
	}

}
