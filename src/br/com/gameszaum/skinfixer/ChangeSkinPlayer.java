package br.com.gameszaum.skinfixer;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.properties.Property;

public class ChangeSkinPlayer {
	
	public static HashMap<CraftPlayer, Double> health = new HashMap<>();
	public static HashMap<CraftPlayer, Location> loc = new HashMap<>();
	
	public static void changeSkin(CraftPlayer cp, String nameFromPlayer){
		GameProfile skingp = cp.getProfile();
		
		try {
			skingp = GameProfileBuilder.fetch(UUIDFetcher.getUUID(nameFromPlayer));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Collection<Property> props = skingp.getProperties().get("textures");
		cp.getProfile().getProperties().removeAll("textures");
		cp.getProfile().getProperties().putAll("textures",props);
		loc.put(cp, cp.getLocation().add(0, 0, 0));
		health.put(cp, cp.getHealth());
		PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(cp.getEntityId());
		sendPacket(destroy);
		cp.getHandle().setHealth(0);
		cp.spigot().respawn();
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				cp.setHealth(health.get(cp));
				cp.teleport(loc.get(cp));
				PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(cp.getHandle());
				for(Player all : Bukkit.getOnlinePlayers()){
					if(!all.getName().equals(cp.getName()))
						((CraftPlayer)all).getHandle().playerConnection.sendPacket(spawn);
				}
			}
		}.runTaskLater(Main.getPlugin(Main.class), 1);
	}
	
	@SuppressWarnings("deprecation")
	public static void sendPacket(Packet packet){
		for(Player all: Bukkit.getOnlinePlayers()){
			((CraftPlayer)all).getHandle().playerConnection.sendPacket(packet);
		}
	}
}
