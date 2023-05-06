package ru.entryset.chat.main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.entryset.api.redis.Redis;
import ru.entryset.chat.mysql.MySQLExecutor;

import java.time.Instant;
import java.util.UUID;

public class Subject{

    private Redis redis;

    public Subject(Redis redis) {
        this.redis = redis;
    }

    public Redis getRedis() {
        return redis;
    }

    synchronized void msg(String base, String context){
        Bukkit.getScheduler().callSyncMethod(Main.getInstance(), () -> {
            for(Player player : Bukkit.getOnlinePlayers()){
                player.sendMessage(base.replace("<context>", context));
            }
            return null;
        });
    }

    synchronized void update(String uuid, String time){
        Long T = Long.decode(time);
        UUID U = UUID.fromString(uuid);
        if(Main.getInstance().time.containsKey(U)){
            T = T + Main.getInstance().time.get(U);
        }
        Main.getInstance().time.put(U,T);
        Bukkit.getScheduler().callSyncMethod(Main.getInstance(), ()-> {
            MySQLExecutor.update(); return null;});
    }

    synchronized void updateLastMessage(String uuid){
        Main.getInstance().lastMessage.put(UUID.fromString(uuid), Instant.now());
    }

}
