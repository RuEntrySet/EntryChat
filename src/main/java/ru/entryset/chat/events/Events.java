package ru.entryset.chat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.entryset.chat.main.Main;
import ru.entryset.chat.message.Message;
import ru.entryset.chat.message.Utils;

import java.time.Duration;
import java.time.Instant;

public class Events implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Main.getInstance().lastMap.put(e.getPlayer(), Instant.now());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        Instant last = Main.getInstance().lastMap.get(player);
        Instant now = Instant.now();
        Duration duration = Duration.between(last, now);
        Utils.update(player.getUniqueId(), duration.getSeconds());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e){

        Player player = e.getPlayer();

        if(!player.hasPermission(Main.config.getPermission("admin")) && !player.hasPermission(Main.config.getPermission("time"))){
            long time = Duration.between(Main.getInstance().lastMap.get(player), Instant.now()).getSeconds();
            if(Main.getInstance().time.containsKey(player.getUniqueId())){
                time = time + Main.getInstance().time.get(player.getUniqueId());
            }
            if(time < Main.config.getInt("settings.first_time")){
                Main.messager.sendMessage(player, Main.config.getMessage("left")
                                .replace("<time>", Utils.format(Main.config.getInt("settings.first_time") - time))
                , true);
                e.setCancelled(true);
                return;
            }
        }

        if(e.isCancelled()){
            return;
        }

        String message = e.getMessage();

        if(message.contains("=")){
            Main.messager.sendMessage(player, "Нельзя писать в чат знак &b=", true);
            e.setCancelled(true);
            return;
        }

        if(!player.hasPermission(Main.config.getPermission("cooldown"))){
            if(Main.getInstance().lastMessage.containsKey(player.getUniqueId())){
                Instant last = Main.getInstance().lastMessage.get(player.getUniqueId());
                Instant now = Instant.now();
                long left = Duration.between(last, now).getSeconds();
                if(left < Main.config.getInt("settings.cooldown_time")){
                    Main.messager.sendMessage(player, Main.config.getMessage("cooldown")
                            .replace("<time>",  Utils.format(Main.config.getInt("settings.cooldown_time") - left)));
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
