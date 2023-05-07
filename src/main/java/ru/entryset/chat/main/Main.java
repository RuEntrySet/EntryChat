package ru.entryset.chat.main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPubSub;
import ru.entryset.api.bukkit.configuration.Config;
import ru.entryset.api.bukkit.manager.Messager;
import ru.entryset.api.redis.Redis;
import ru.entryset.chat.mysql.MySQLExecutor;
import ru.entryset.chat.events.Events;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin {

    public static Config config;

    private static Main instance;

    public static Messager messager;

    public static Redis redis;

    public HashMap<UUID, Long> time = new HashMap<>();

    public HashMap<Player, Instant> lastMap = new HashMap<>();

    public HashMap<UUID, Instant> lastMessage = new HashMap<>();

    public static Subject subject;

    @Override
    public void onEnable() {
        instance = this;
        config = new Config(this, "config.yml");
        messager = new Messager(config);
        redis = config.getRedis("redis");
        subject = new Subject(config.getRedis("redis"));
        MySQLExecutor.createTableProducts();
        registerEvents();
        sub();
        MySQLExecutor.check();
        for(Player player : Bukkit.getOnlinePlayers()){
            Main.getInstance().lastMap.put(player, Instant.now());
        }
    }

    public void sub(){
        Runnable task = () -> {
            JedisPubSub jedisPubSub = new JedisPubSub() {

                @Override
                public void onMessage(String channel, String message) {
                    if (channel.equals("EntryChat")) {
                        String[] parts = message.split("=");
                        switch (parts[0]){
                            case "msg":
                                Bukkit.getScheduler().callSyncMethod(Main.getInstance(), () -> {
                                subject.msg(parts[1], parts[2]);
                                return null;
                            });
                                break;
                            case "last": subject.updateLastMessage(parts[1]);
                                break;
                            case "update": subject.update(parts[1], parts[2]);
                                break;
                        }
                    }
                }
            };
            if(subject.getRedis().getJedis().isConnected()){
                subject.getRedis().getJedis().subscribe(jedisPubSub, "EntryChat");
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void onDisable(){
        MySQLExecutor.update2();
        redis.close();
        subject.getRedis().close();
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new Events(), this);
    }

    public static Main getInstance() {
        return Main.instance;
    }

}
