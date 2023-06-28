package ru.entryset.chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.entryset.chat.mysql.MySQLExecutor;
import ru.entryset.chat.events.Events;
import ru.entryset.core.EntryCore;
import ru.entryset.core.bukkit.bukkit.configuration.Config;
import ru.entryset.core.bukkit.bukkit.manager.Messager;
import ru.entryset.core.expansion.Expansion;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

public class EntryChat extends Expansion {

    public static Config config;

    private static EntryChat instance;

    public static Messager messager;

    public HashMap<UUID, Long> time = new HashMap<>();

    public HashMap<Player, Instant> lastMap = new HashMap<>();

    public HashMap<UUID, Instant> lastMessage = new HashMap<>();

    @Override
    public void init() {
        instance = this;
        config = new Config(this, "EntryChat.yml");
        messager = new Messager(config);
        MySQLExecutor.createTableProducts();
        registerEvents();
        MySQLExecutor.check();
        for(Player player : Bukkit.getOnlinePlayers()){
            EntryChat.getInstance().lastMap.put(player, Instant.now());
        }
    }

    @Override
    public void destruct() {
        MySQLExecutor.update();
    }

    private void registerEvents() {
        EntryCore.getInstance().getRegistryManager().registerHandlers(this, new Events());
    }

    public static EntryChat getInstance() {
        return EntryChat.instance;
    }

}
