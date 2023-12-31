package ru.entryset.chat.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.entryset.chat.EntryChat;
import ru.entryset.chat.message.Message;
import ru.entryset.chat.message.Utils;
import ru.entryset.chat.mysql.MySQLExecutor;
import ru.entryset.core.EntryCore;
import ru.entryset.core.api.CarrotPushEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class Events implements Listener {

    @EventHandler
    private void onPush(CarrotPushEvent e){
       if(!e.isCurrent(EntryChat.getInstance())) return;
        String[] parts = e.getPacket().getInfo().toString().split("=");
        switch (parts[0]){
            case "msg":
                Bukkit.getScheduler().callSyncMethod(EntryCore.getInstance(), () -> {
                    msg(parts[1], parts[2]);
                    return null;
                });
                break;
            case "last": updateLastMessage(parts[1]);
                break;
            case "update": update(parts[1], parts[2]);
                break;
        }
    }

    synchronized void msg(String base, String context){
        Bukkit.getScheduler().callSyncMethod(EntryCore.getInstance(), () -> {
            for(Player player : Bukkit.getOnlinePlayers()){
                player.sendMessage(base.replace("<context>", context));
            }
            return null;
        });
    }

    synchronized void update(String uuid, String time){
        Long T = Long.decode(time);
        UUID U = UUID.fromString(uuid);
        if(EntryChat.getInstance().time.containsKey(U)){
            T = T + EntryChat.getInstance().time.get(U);
        }
        EntryChat.getInstance().time.put(U,T);
        Bukkit.getScheduler().callSyncMethod(EntryCore.getInstance(), ()-> {
            MySQLExecutor.update(); return null;});
    }

    synchronized void updateLastMessage(String uuid){
        EntryChat.getInstance().lastMessage.put(UUID.fromString(uuid), Instant.now());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        EntryChat.getInstance().lastMap.put(e.getPlayer(), Instant.now());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        Instant last = EntryChat.getInstance().lastMap.get(player);
        Instant now = Instant.now();
        Duration duration = Duration.between(last, now);
        Utils.update(player.getUniqueId(), duration.getSeconds());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e){

        Player player = e.getPlayer();

        if(!player.hasPermission(EntryChat.config.getPermission("admin")) && !player.hasPermission(EntryChat.config.getPermission("time")) && e.getMessage().startsWith("!")){
            long time = Duration.between(EntryChat.getInstance().lastMap.get(player), Instant.now()).getSeconds();
            if(EntryChat.getInstance().time.containsKey(player.getUniqueId())){
                time = time + EntryChat.getInstance().time.get(player.getUniqueId());
            }
            if(time < EntryChat.config.getInt("settings.first_time")){
                EntryChat.messager.sendMessage(player, EntryChat.config.getMessage("left")
                                .replace("<time>", Utils.format(EntryChat.config.getInt("settings.first_time") - time)));
                e.setCancelled(true);
                return;
            }
        }

        if(e.isCancelled()){
            return;
        }

        String message = e.getMessage();

        if(message.contains("=")){
            EntryChat.messager.sendMessage(player, EntryChat.config.getMessage("do_not"));
            e.setCancelled(true);
            return;
        }

        if(!player.hasPermission(EntryChat.config.getPermission("cooldown")) && !e.getMessage().startsWith("!")){
            if(EntryChat.getInstance().lastMessage.containsKey(player.getUniqueId())){
                Instant last = EntryChat.getInstance().lastMessage.get(player.getUniqueId());
                Instant now = Instant.now();
                long left = Duration.between(last, now).getSeconds();
                if(left < EntryChat.config.getInt("settings.cooldown_time")){
                    EntryChat.messager.sendMessage(player, EntryChat.config.getMessage("cooldown")
                            .replace("<time>",  Utils.format(EntryChat.config.getInt("settings.cooldown_time") - left)));
                    e.setCancelled(true);
                    return;
                }
            }
        }

        Message content = new Message(player, message);
        content.send();
        e.setCancelled(true);
    }

}
