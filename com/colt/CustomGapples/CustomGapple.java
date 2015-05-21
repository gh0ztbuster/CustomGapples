package com.colt.CustomGapple;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CustomGapple extends JavaPlugin implements Listener {
	
	List<String> effects = getConfig().getStringList("effects");
	int dur;
	int amp;
	
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		saveDefaultConfig();
	}
	
	@EventHandler
	public void playerFoodConsume(PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();	
		if(e.getItem().getType() == Material.GOLDEN_APPLE && e.getItem().getDurability() == 1) {
			if(p.hasPermission("customgapple.use")) {		
				p.getInventory().removeItem(new ItemStack(Material.GOLDEN_APPLE, 1, (short)1));
				if(p.getFoodLevel() <= 18) {
					p.setFoodLevel(p.getFoodLevel() + 4);
				}	
				e.setCancelled(true);
				for(String po : effects) {
					String inf[] = po.split(":");
					if(inf.length == 3) {
						if(PotionEffectType.getByName(inf[0].toUpperCase()) != null) {
							PotionEffectType pot = PotionEffectType.getByName(inf[0].toUpperCase());
							try {
								dur = Math.min(Integer.parseInt(inf[1]), Integer.MAX_VALUE);
							} catch(NumberFormatException e1) {
								dur = Integer.MAX_VALUE;
							}
							try {
								amp = Math.min(Integer.parseInt(inf[2]), 255);
							} catch(NumberFormatException e1) {
								amp = 255;
							}
							if(!p.hasPotionEffect(pot)) {
								PotionEffect effect = new PotionEffect(pot, dur, amp);
								p.addPotionEffect(effect);	
							} else {
								p.removePotionEffect(pot);
								PotionEffect effect = new PotionEffect(pot, dur, amp);
								p.addPotionEffect(effect);
							}
						}
					}
				}
			}
		}	
	}	
}
