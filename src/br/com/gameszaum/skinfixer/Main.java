package br.com.gameszaum.skinfixer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	/*
	* Sistema de skin do @gameszaum_,
	* se for usar pelo menos deixa os créditos ;-;
	*/

	public void onEnable() {
		saveDefaultConfig();
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getConsoleSender().sendMessage("§b§lSkinFixer §aactived with sucess! §e@gameszaum_");
	}
	
	@EventHandler
	public void joinPlayerEvent(PlayerJoinEvent e){
		if(isPremium(e.getPlayer().getName())){
			ChangeSkinPlayer.changeSkin((CraftPlayer)e.getPlayer(), e.getPlayer().getName());
		}else{
			ChangeSkinPlayer.changeSkin((CraftPlayer)e.getPlayer(), getConfig().getString("Skin"));
		}
	}
	public boolean isPremium(String userName) {
		boolean isPremium = true;
		try {
			URL e = new URL("https://api.mojang.com/users/profiles/minecraft/" + userName);
			InputStream stream = e.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			if (br.readLine().equals("true")) {
				isPremium = true;
			}
			br.close();
			stream.close();
		} catch (Exception arg) {
			System.out.println("Error to know if " + userName + " is premium.");
		}

		return isPremium;
	}
}